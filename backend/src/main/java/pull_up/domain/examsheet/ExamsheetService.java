package pull_up.domain.examsheet;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import pull_up.domain.dao.ExamsheetRepository;
import pull_up.domain.examsheet.dto.CreateExamsheet;
import pull_up.api.dto.MessageDto;
import pull_up.domain.examsheet.dto.ExamsheetDetailInfo;
import pull_up.domain.problem.dto.ProblemDetailInfo;
import pull_up.domain.problem.exception.ProblemErrorCode;
import pull_up.domain.problem.exception.ProblemException;
import pull_up.infra.database.jpa.dto.ExamsheetInfo;
import pull_up.infra.database.jpa.dto.SearchParam;
import pull_up.infra.database.jpa.entity.Examsheet;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamsheetService {

    private final ExamsheetRepository examsheetRepository;

    public MessageDto createExamsheet(CreateExamsheet.Request createExamsheetReq) {
        Examsheet examsheet = Examsheet.create(createExamsheetReq.examTitle(), createExamsheetReq.getProblemMap());
        examsheetRepository.save(examsheet);
        return new MessageDto("시험 문제 [id : " + examsheet.getId() + ", 제목 : " + examsheet.getExamTitle() + "] 가 정상 생성되었습니다.");
    }

    public List<ExamsheetInfo> getAll() {
        return examsheetRepository.searchExamsheet();
    }

    public ExamsheetDetailInfo get(Long examsheetId) {
        return examsheetRepository.findByIdWithProblem(examsheetId)
                .orElseThrow(() -> new ProblemException(ProblemErrorCode.NOT_FOUND_PROBLEM));
    }
}
