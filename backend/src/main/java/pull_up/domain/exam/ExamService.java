package pull_up.domain.exam;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pull_up.domain.dao.ExamRepository;
import pull_up.domain.exam.dto.*;
import pull_up.domain.exam.exception.ExamErrorCode;
import pull_up.domain.exam.exception.ExamException;
import pull_up.domain.dao.MemberRepository;
import pull_up.domain.dao.ProblemRepository;
import pull_up.domain.examsheet.ExamsheetRepository;
import pull_up.domain.member.exception.MemberException;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.Answer;
import pull_up.infra.database.jpa.entity.Exam;
import pull_up.infra.database.jpa.entity.Member;
import pull_up.infra.database.jpa.entity.Problem;

import java.util.List;
import java.util.Map;

import static pull_up.domain.member.exception.MemberErrorCode.NOT_FOUND_MEMBER;

@RequiredArgsConstructor
@Service
public class ExamService {

    private final ExamRepository examRepository;
    private final MemberRepository memberRepository;
    private final ProblemRepository problemRepository;
    private final ExamsheetRepository examsheetRepository;

    public SolvedInfo.Response getSolvedInfo(Long memberId, Entry entry) {
        List<Exam> solvedExamsInDB = examRepository.findAllByMemberIdAndEntry(memberId, entry);
        Map<String, Integer> problemTypesMap = problemRepository.findAllProblemTypeAndCountByEntry(entry);
        return SolvedInfo.Response.toDto(entry, solvedExamsInDB, problemTypesMap);
    }

    public Start.Response start(Start.EvenlyRequest startReq) {
        Member startMember = memberRepository.findById(startReq.memberId())
                .orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER));

        List<Problem> problemList = problemRepository.findAllByEntry(startReq.entry());
        Exam startedExam = Exam.start(ExamType.EVENLY, startMember, problemList);
        examRepository.save(startedExam);

        return Start.Response.toDto(startedExam);
    }

    public Start.Response start(Start.ByProblemTypeRequest startReq) {
        Member startMember = memberRepository.findById(startReq.memberId())
                .orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER));

        List<Problem> problemList = problemRepository.findAllByEntryAndProblemType(startReq.entry(), startReq.problemType());
        Exam startedExam = Exam.start(ExamType.BY_PROBLEM_TYPE, startMember, problemList);
        examRepository.save(startedExam);

        return Start.Response.toDto(startedExam);
    }

    public Start.Response start(Start.MockExamRequest startReq) {
        Member startMember = memberRepository.findById(startReq.memberId())
                .orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER));

        Map<Integer, Long> examSheet = examsheetRepository.findByExamTitle("모의고사").getProblemMap();
        List<Problem> problemList = problemRepository.findAllById(examSheet.values());
        Exam startedExam = Exam.startMockExam(ExamType.MOCK_EXAM, startMember, examSheet, problemList);

        return Start.Response.toDto(null);
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

    public Grade.Response grade(Grade.Request gradeReq) {
        return null;
    }

    public End.Response end(Long examId) {
        Exam examInDB = examRepository.findById(examId)
                .orElseThrow(() -> new ExamException(ExamErrorCode.NOT_FOUND_EXAM));

        examInDB.end();

        return End.Response.toDto(examInDB);
    }
}
