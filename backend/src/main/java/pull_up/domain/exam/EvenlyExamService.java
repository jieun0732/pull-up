package pull_up.domain.exam;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pull_up.api.exam.evenly.dto.Next;
import pull_up.api.exam.evenly.dto.Start;
import pull_up.api.exam.evenly.dto.Submit;
import pull_up.domain.answer.AnswerRepository;
import pull_up.domain.member.MemberRepository;
import pull_up.domain.problem.ProblemRepository;
import pull_up.global.exception.member.MemberException;
import pull_up.infra.database.entity.Exam;
import pull_up.infra.database.entity.Member;
import pull_up.infra.database.entity.Problem;

import java.util.List;

import static pull_up.global.exception.member.MemberErrorCode.NOT_FOUND_MEMBER;

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

        return Start.Response.toDto(Exam.start(startReq.examType(), startMember, problemList));
    }

    public Submit.Response submit(Object submitReq1) {
        return new Submit.Response();
    }

    public Next.Response next(Long memberId, Long examId, Long answerId) {
        return new Next.Response();
    }

    public Object end(Object endReq) {
        return new Object();
    }
}
