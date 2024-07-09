package pull_up.api.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pull_up.api.member.dto.MemberAnswerDto;
import pull_up.api.member.entity.Member;
import pull_up.api.member.entity.MemberAnswer;
import pull_up.api.member.exception.MemberErrorCode;
import pull_up.api.member.exception.MemberException;
import pull_up.api.member.repository.MemberAnswerRepository;
import pull_up.api.member.repository.MemberRepository;
import pull_up.api.problem.entity.Problem;
import pull_up.api.problem.exception.ProblemErrorCode;
import pull_up.api.problem.exception.ProblemException;
import pull_up.api.problem.repository.ProblemRepository;

@Service
public class MemberAnswerService {

    @Autowired
    private MemberAnswerRepository memberAnswerRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private MemberRepository memberRepository;

    public MemberAnswerDto submitAnswer(Long memberId, Long problemId, Long examId, String chosenAnswer) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER));
        Problem problem = problemRepository.findById(problemId)
            .orElseThrow(() -> new ProblemException(ProblemErrorCode.NOT_FOUND_PROBLEM));

        boolean isCorrect = problem.getAnswer().equals(chosenAnswer);

        MemberAnswer memberAnswer = MemberAnswer.of(member, problem, null, chosenAnswer, isCorrect);
        memberAnswer = memberAnswerRepository.save(memberAnswer);

        return MemberAnswerDto.from(memberAnswer);
    }
}
