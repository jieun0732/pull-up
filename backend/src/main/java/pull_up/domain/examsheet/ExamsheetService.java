package pull_up.domain.examsheet;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pull_up.domain.dao.ExamsheetRepository;
import pull_up.domain.dao.ProblemRepository;
import pull_up.domain.examsheet.dto.CreateExamsheet;
import pull_up.api.dto.MessageDto;
import pull_up.domain.examsheet.dto.ExamsheetDetailInfo;
import pull_up.domain.examsheet.dto.ExamsheetInfo;
import pull_up.domain.examsheet.exception.ExamsheetErrorCode;
import pull_up.domain.examsheet.exception.ExamsheetException;
import pull_up.domain.problem.dto.ProblemInfo;
import pull_up.infra.database.jpa.dto.SearchParam;
import pull_up.infra.database.jpa.embedded.Problemsheet;
import pull_up.infra.database.jpa.entity.Examsheet;
import pull_up.infra.database.jpa.entity.Problem;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExamsheetService {

    private final ExamsheetRepository examsheetRepository;
    private final ProblemRepository problemRepository;

    @Transactional
    public MessageDto createExamsheet(CreateExamsheet.Request createExamsheetReq) {
        Examsheet examsheet = Examsheet.create(createExamsheetReq.examTitle(), createExamsheetReq.getProblemMap());
        examsheetRepository.save(examsheet);
        return new MessageDto("시험 문제 [id : " + examsheet.getId() + ", 제목 : " + examsheet.getExamTitle() + "] 가 정상 생성되었습니다.");
    }

    @Transactional
    public void createEmptyExamsheet(String examTitle, Integer problemCount) {
        Examsheet examsheet = Examsheet.createEmpty(examTitle, problemCount);
        examsheetRepository.save(examsheet);
    }

    public List<ExamsheetInfo> getAll() {
        return examsheetRepository.searchExamsheet();
    }

    public ExamsheetDetailInfo get(Long examsheetId) {
        Examsheet examsheet = examsheetRepository.findByIdWithProblem(examsheetId).orElseThrow(() -> new ExamsheetException(ExamsheetErrorCode.NOT_FOUND_EXAMSHEET));
        List<Long> ids = examsheet.getProblemsheets().stream().map(Problemsheet::getProblemId).toList();
        List<Problem> problems = problemRepository.findAllById(ids);

        return ExamsheetDetailInfo.toDto(examsheet, examsheet.getProblemEntityMap(problems));
    }

    public Page<ProblemInfo> getAllProblems(SearchParam searchParam) {
        return problemRepository.searchProblem(searchParam);
    }

    @Transactional
    public ExamsheetDetailInfo update(Long examsheetId, Map<String, String> parameters) {
        Examsheet examsheet = examsheetRepository.findById(examsheetId).orElseThrow(() -> new ExamsheetException(ExamsheetErrorCode.NOT_FOUND_EXAMSHEET));
        examsheet.modify(parameters);

        List<Long> ids = examsheet.getProblemsheets().stream().map(Problemsheet::getProblemId).toList();
        List<Problem> problems = problemRepository.findAllById(ids);

        return ExamsheetDetailInfo.toDto(examsheet, examsheet.getProblemEntityMap(problems));
    }

    @Transactional
    public ExamsheetDetailInfo changeProblem(Long examsheetId, Integer problemNumber, Long newSelectedId) {
        Examsheet examsheet = examsheetRepository.findById(examsheetId).orElseThrow(() -> new ExamsheetException(ExamsheetErrorCode.NOT_FOUND_EXAMSHEET));
        examsheet.changeProblem(problemNumber, newSelectedId);

        List<Long> ids = examsheet.getProblemsheets().stream().map(Problemsheet::getProblemId).toList();
        List<Problem> problems = problemRepository.findAllById(ids);

        return ExamsheetDetailInfo.toDto(examsheet, examsheet.getProblemEntityMap(problems));
    }

    @Transactional
    public void delete(Long examsheetId) {
        Examsheet examsheet = examsheetRepository.findById(examsheetId).orElseThrow(() -> new ExamsheetException(ExamsheetErrorCode.NOT_FOUND_EXAMSHEET));
        examsheetRepository.delete(examsheet);
    }
}
