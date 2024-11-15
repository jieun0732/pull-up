package pull_up.domain.member;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pull_up.api.exam.dto.ExamInformationDto;
import pull_up.infra.database.entity.Exam;
import pull_up.infra.database.repository.exam.ExamRepository;
import pull_up.api.member.dto.MemberAnswerDto;
import pull_up.api.member.dto.MemberAnswerResultDto;
import pull_up.api.member.dto.MemberDto;
import pull_up.infra.database.entity.Member;
import pull_up.infra.database.entity.legacy.MemberAnswer;
import pull_up.global.exception.member.MemberErrorCode;
import pull_up.global.exception.member.MemberException;
import pull_up.infra.database.repository.legacy.MemberAnswerRepository;
import pull_up.infra.database.repository.member.MemberRepository;
import pull_up.api.problem.dto.ProblemDto;
import pull_up.infra.database.entity.Problem;
import pull_up.global.exception.problem.ProblemErrorCode;
import pull_up.global.exception.problem.ProblemException;
import pull_up.domain.problem.ProblemRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberAnswerService {

    private final MemberAnswerRepository memberAnswerRepository;

    private final ProblemRepository problemRepository;

    private final MemberRepository memberRepository;

    public void createMemberAnswersForNonMockExamProblems(Long memberId) {
        // Member 조회
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        // category가 "모의고사"가 아닌 모든 문제 조회
        List<Problem> problems = problemRepository.findByCategoryNot("모의고사");

        // MemberAnswer 생성 및 저장
        for (Problem problem : problems) {
            MemberAnswer memberAnswer = MemberAnswer.of(member, problem, null, null, false);
            memberAnswerRepository.save(memberAnswer);
        }
    }
}
