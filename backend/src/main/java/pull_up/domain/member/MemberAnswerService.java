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

    private final ExamRepository examRepository;

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

    @Transactional
    public MemberAnswerResultDto saveMemberAnswer(MemberDto memberDTO, ProblemDto problemDTO, ExamInformationDto examInformationDTO, String chosenAnswer) {
        Member member = MemberDto.toEntity(memberDTO);
        Problem problem = problemRepository.findById(problemDTO.id()).orElseThrow();
        Exam exam = examRepository.findById(examInformationDTO.id()).orElseThrow();
        boolean isCorrect = chosenAnswer.equals(problem.getAnswer());

        problem.setTotalAttempts(problem.getTotalAttempts() + 1);
        if (!isCorrect) {
            problem.setIncorrectAttempts(problem.getIncorrectAttempts() + 1);
        }
        problem.setIncorrectRate((double) problem.getIncorrectAttempts() / problem.getTotalAttempts() * 100);
        problemRepository.save(problem);

        MemberAnswer memberAnswer = MemberAnswer.of(member, problem, exam, chosenAnswer, isCorrect);
        memberAnswerRepository.save(memberAnswer);

        return MemberAnswerResultDto.from(memberAnswer);
    }

    public List<MemberAnswerResultDto> getIncorrectAnswers(MemberDto memberDTO, String category, String entry, String type) {
        Member member = MemberDto.toEntity(memberDTO);
        List<MemberAnswer> incorrectAnswers = memberAnswerRepository.findIncorrectAnswers(member, category, entry, type);
        return incorrectAnswers.stream().map(MemberAnswerResultDto::from).collect(Collectors.toList());
    }

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
