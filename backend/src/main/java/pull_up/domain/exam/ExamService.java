package pull_up.domain.exam;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pull_up.api.dto.MessageDto;
import pull_up.domain.dao.ExamRepository;
import pull_up.domain.exam.dto.*;
import pull_up.domain.exam.exception.ExamErrorCode;
import pull_up.domain.exam.exception.ExamException;
import pull_up.domain.dao.MemberRepository;
import pull_up.domain.dao.ProblemRepository;
import pull_up.domain.dao.ExamsheetRepository;
import pull_up.domain.member.exception.MemberException;
import pull_up.infra.database.jpa.dto.SearchParam;
import pull_up.infra.database.jpa.entity.*;

import java.util.List;
import java.util.Optional;

import static pull_up.domain.member.exception.MemberErrorCode.NOT_FOUND_MEMBER;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ExamService {

    private final ExamRepository examRepository;
    private final MemberRepository memberRepository;
    private final ProblemRepository problemRepository;
    private final ExamsheetRepository examsheetRepository;

    public Page<ExamInfo> getAll(SearchParam searchParam) {
        return examRepository.searchExam(searchParam);
    }

    @Transactional
    public Start.Response start(Start.EvenlyRequest startReq) {
        String examTitle = "EVENLY_" + startReq.entry();

        Optional<Exam> exam = examRepository.findEvenlyExamByMemberIdAndExamTitle(startReq.memberId(), examTitle);

        Exam newExam = exam.orElseGet(() -> {
            Member startMember = memberRepository.findById(startReq.memberId()).orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER));
            Examsheet examSheet = examsheetRepository.findByExamTitle(examTitle);
            List<Problem> problemList = problemRepository.findAllById(examSheet.getProblemMap().values());

            Exam startedExam = Exam.startEvenlyExam(startMember, problemList, examSheet);
            examRepository.save(startedExam);
            return startedExam;
        });

        return Start.Response.toDto(newExam);
    }

    @Transactional
    public Start.Response start(Start.ByProblemTypeRequest startReq) {
        Member startMember = memberRepository.findById(startReq.memberId()).orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER));
        List<Problem> problemList = problemRepository.findAllByEntryAndProblemTypeExceptProblemsheet(startReq.entry(), startReq.problemType());
        Exam startedExam = Exam.startByProblemTypeExam(startMember, problemList);
        examRepository.save(startedExam);
        return Start.Response.toDto(startedExam);
    }

    @Transactional
    public Start.Response start(Start.MockExamRequest startReq) {
        Optional<Exam> exam = examRepository.findMockExamByMemberId(startReq.memberId());

        exam.ifPresent(Exam::reset);
        Exam newExam = exam.orElseGet(() -> {
            Member startMember = memberRepository.findById(startReq.memberId()).orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER));
            Examsheet examSheet = examsheetRepository.findByExamTitle(startReq.mockExamName());
            List<Problem> problemList = problemRepository.findAllById(examSheet.getProblemMap().values());

            Exam startedExam = Exam.startMockExam(startMember, problemList, examSheet);
            examRepository.save(startedExam);
            return startedExam;
        });

        return Start.Response.toDto(newExam);
    }

    @Transactional
    public Explanation submit(Submit.Request submitReq) {
        Exam examInDB = examRepository.findById(submitReq.examId())
                .orElseThrow(() -> new ExamException(ExamErrorCode.NOT_FOUND_EXAM));
        Answer answer = examInDB.submit(submitReq.problemNumber(), submitReq.submitAnswer());
        return Explanation.toDto(answer);
    }

    public Next.Response next(Long examId, Integer problemNumber) {
        Exam examInDB = examRepository.findById(examId)
                .orElseThrow(() -> new ExamException(ExamErrorCode.NOT_FOUND_EXAM));
        return Next.Response.toDto(examInDB, problemNumber);
    }

    public Next.Response continueExam(Long examId) {
        Exam examInDB = examRepository.findById(examId)
                .orElseThrow(() -> new ExamException(ExamErrorCode.NOT_FOUND_EXAM));
        return Next.Response.toDto(examInDB, examInDB.getLastSolvedProblem() + 1);
    }

    @Transactional
    public Report.mockExam grade(Grade.Request gradeReq) {
        Exam examInDB = examRepository.findById(gradeReq.examId()).orElseThrow(() -> new ExamException(ExamErrorCode.NOT_FOUND_EXAM));
        examInDB.grade(gradeReq.getAnswerSheet());
        return Report.mockExam.toDto(examInDB);
    }

    public Report.mockExam getMockExamReport(Long examId) {
        Exam examInDB = examRepository.findById(examId).orElseThrow(() -> new ExamException(ExamErrorCode.NOT_FOUND_EXAM));
        return Report.mockExam.toDto(examInDB);
    }

    public Result.ByEntryResponse getEntryExamResult(Long examId) {
        Exam examInDB = examRepository.findById(examId).orElseThrow(() -> new ExamException(ExamErrorCode.NOT_FOUND_EXAM));
        return Result.ByEntryResponse.toDto(examInDB);
    }

    public Result.MockExamResponse getMockExamResult(Long examId) {
        Exam examInDB = examRepository.findById(examId).orElseThrow(() -> new ExamException(ExamErrorCode.NOT_FOUND_EXAM));
        if (!examInDB.getIsFinished()) throw new ExamException(ExamErrorCode.NOT_GRADED_MOCK_EXAM);
        return Result.MockExamResponse.toDto(examInDB);
    }

    @Transactional
    public MessageDto reset(Long examId) {
        Exam examInDB = examRepository.findById(examId)
                .orElseThrow(() -> new ExamException(ExamErrorCode.NOT_FOUND_EXAM));
        examRepository.delete(examInDB);
        return new MessageDto(examId + "번 시험이 리셋되었습니다.");
    }
}
