package pull_up.domain.exam;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pull_up.api.dto.MessageDto;
import pull_up.domain.dao.ExamRepository;
import pull_up.domain.exam.dto.*;
import pull_up.domain.exam.exception.ExamErrorCode;
import pull_up.domain.exam.exception.ExamException;
import pull_up.domain.dao.MemberRepository;
import pull_up.domain.dao.ProblemRepository;
import pull_up.domain.dao.ExamsheetRepository;
import pull_up.domain.member.exception.MemberException;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static pull_up.domain.member.exception.MemberErrorCode.NOT_FOUND_MEMBER;

@RequiredArgsConstructor
@Service
public class ExamService {

    private final ExamRepository examRepository;
    private final MemberRepository memberRepository;
    private final ProblemRepository problemRepository;
    private final ExamsheetRepository examsheetRepository;

    public Solved.ByEntry.Response getSolvedInfo(Long memberId, Entry entry) {
        Map<String, Exam> examMap = examRepository.findAllEvenlyAndProblemTypeExamMap(memberId, entry);
        Map<String, Integer> problemTypesMap = problemRepository.findAllProblemTypeAndCountByEntry(entry);

        for (Map.Entry<String, Integer> problemTypeEntry : problemTypesMap.entrySet()) {
            if (examMap.containsKey(problemTypeEntry.getKey())) continue;
            examMap.put(problemTypeEntry.getKey(), new TempExam(problemTypeEntry.getValue()));
        }

        return Solved.ByEntry.Response.toDto(entry, examMap);
    }

    public Solved.MockExam.Response getSolvedInfo(Long memberId) {
        Optional<Exam> exam = examRepository.findByMemberId(memberId);
        return exam.map(Solved.MockExam.Response::toDto)
                .orElseGet(Solved.MockExam.Response::empty);
    }

    public Start.Response start(Start.EvenlyRequest startReq) {
        Member startMember = memberRepository.findById(startReq.memberId())
                .orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER));
        List<Problem> problemList = problemRepository.findAllByEntry(startReq.entry());

        Exam startedExam = Exam.startEvenlyExam(startMember, problemList);

        examRepository.save(startedExam);

        return Start.Response.toDto(startedExam);
    }

    public Start.Response start(Start.ByProblemTypeRequest startReq) {
        Member startMember = memberRepository.findById(startReq.memberId())
                .orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER));
        List<Problem> problemList = problemRepository.findAllByEntryAndProblemType(startReq.entry(), startReq.problemType());

        Exam startedExam = Exam.startByProblemTypeExam(startMember, problemList);

        examRepository.save(startedExam);

        return Start.Response.toDto(startedExam);
    }

    public Start.Response start(Start.MockExamRequest startReq) {
        Member startMember = memberRepository.findById(startReq.memberId())
                .orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER));
        Examsheet examSheet = examsheetRepository.findByExamTitle("모의고사");
        List<Problem> problemList = problemRepository.findAllById(examSheet.getProblemMap().values());

        Exam startedExam = Exam.startMockExam(startMember, problemList, examSheet);

        examRepository.save(startedExam);

        return Start.Response.toDto(startedExam);
    }

    public Submit.Response submit(Submit.Request submitReq) {
        Exam examInDB = examRepository.findById(submitReq.examId())
                .orElseThrow(() -> new ExamException(ExamErrorCode.NOT_FOUND_EXAM));

        Answer answer = examInDB.submit(submitReq.problemNumber(), submitReq.submitAnswer());

        return Submit.Response.toDto(answer);
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

    public Grade.Response grade(Grade.Request gradeReq) {
        Exam examInDB = examRepository.findById(gradeReq.examId())
                .orElseThrow(() -> new ExamException(ExamErrorCode.NOT_FOUND_EXAM));

        examInDB.grade(gradeReq.getAnswerSheet());

        return Grade.Response.toDto(examInDB);
    }

    public End.Response end(Long examId) {
        Exam examInDB = examRepository.findById(examId)
                .orElseThrow(() -> new ExamException(ExamErrorCode.NOT_FOUND_EXAM));

        examInDB.end();

        return End.Response.toDto(examInDB);
    }

    public MessageDto reset(Long examId) {
        Exam examInDB = examRepository.findById(examId)
                .orElseThrow(() -> new ExamException(ExamErrorCode.NOT_FOUND_EXAM));

        examRepository.delete(examInDB);

        return new MessageDto(examId + "번 시험이 리셋되었습니다.");
    }
}
