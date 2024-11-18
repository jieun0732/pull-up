package pull_up.domain.exam;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pull_up.domain.dao.ExamRepository;
import pull_up.domain.exam.dto.End;
import pull_up.domain.exam.dto.Next;
import pull_up.domain.exam.dto.Start;
import pull_up.domain.exam.dto.Submit;
import pull_up.domain.dao.AnswerRepository;
import pull_up.domain.exam.exception.ExamErrorCode;
import pull_up.domain.exam.exception.ExamException;
import pull_up.domain.dao.MemberRepository;
import pull_up.domain.dao.ProblemRepository;
import pull_up.domain.member.exception.MemberException;
import pull_up.infra.database.entity.Answer;
import pull_up.infra.database.entity.Exam;
import pull_up.infra.database.entity.Member;
import pull_up.infra.database.entity.Problem;

import java.util.List;

import static pull_up.domain.member.exception.MemberErrorCode.NOT_FOUND_MEMBER;

@RequiredArgsConstructor
@Service
public class EvenlyExamService {

    private final ExamRepository examRepository;
    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;
    private final ProblemRepository problemRepository;

    public Start.Response start(Start.Request startReq) {
        Member startMember = memberRepository.findById(startReq.memberId())
                .orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER));

        List<Problem> problemList = problemRepository.findAllByEntry(startReq.entry());
        Exam startedExam = Exam.start(startReq.examType(), startMember, problemList);
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

    public End.Response end(Long examId) {
        Exam examInDB = examRepository.findById(examId)
                .orElseThrow(() -> new ExamException(ExamErrorCode.NOT_FOUND_EXAM));

        examInDB.end();

        return End.Response.toDto(examInDB);
    }
}
