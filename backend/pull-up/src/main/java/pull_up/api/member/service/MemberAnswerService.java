package pull_up.api.member.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pull_up.api.exam.dto.ExamInformationDto;
import pull_up.api.exam.entity.ExamInformation;
import pull_up.api.exam.repository.ExamInformationRepository;
import pull_up.api.member.dto.MemberAnswerDto;
import pull_up.api.member.dto.MemberDto;
import pull_up.api.member.entity.Member;
import pull_up.api.member.entity.MemberAnswer;
import pull_up.api.member.exception.MemberErrorCode;
import pull_up.api.member.exception.MemberException;
import pull_up.api.member.repository.MemberAnswerRepository;
import pull_up.api.member.repository.MemberRepository;
import pull_up.api.problem.dto.ProblemDto;
import pull_up.api.problem.entity.Problem;
import pull_up.api.problem.exception.ProblemErrorCode;
import pull_up.api.problem.exception.ProblemException;
import pull_up.api.problem.repository.ProblemRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberAnswerService {

    @Autowired
    private MemberAnswerRepository memberAnswerRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ExamInformationRepository examInformationRepository;

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
    public MemberAnswerDto saveMemberAnswer(MemberDto memberDTO, ProblemDto problemDTO, ExamInformationDto examInformationDTO, String chosenAnswer) {
        Member member = MemberDto.toEntity(memberDTO);
        Problem problem = problemRepository.findById(problemDTO.id()).orElseThrow();
        ExamInformation examInformation = examInformationRepository.findById(examInformationDTO.id()).orElseThrow();
        boolean isCorrect = chosenAnswer.equals(problem.getAnswer());

        problem.setTotalAttempts(problem.getTotalAttempts() + 1);
        if (!isCorrect) {
            problem.setIncorrectAttempts(problem.getIncorrectAttempts() + 1);
        }
        problem.setIncorrectRate((double) problem.getIncorrectAttempts() / problem.getTotalAttempts() * 100);
        problemRepository.save(problem);

        MemberAnswer memberAnswer = MemberAnswer.of(member, problem, examInformation, chosenAnswer, isCorrect);
        memberAnswerRepository.save(memberAnswer);

        return toDTO(memberAnswer);
    }

    public List<MemberAnswerDto> getIncorrectAnswers(MemberDto memberDTO, String category, String entry, String type) {
        Member member = MemberDto.toEntity(memberDTO);
        List<MemberAnswer> incorrectAnswers = memberAnswerRepository.findIncorrectAnswers(member, category, entry, type);
        return incorrectAnswers.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private MemberAnswerDto toDTO(MemberAnswer memberAnswer) {
        return new MemberAnswerDto(
                memberAnswer.getId(),
                MemberDto.from(memberAnswer.getMember()),
                new ProblemDto(
                        memberAnswer.getProblem().getId(),
                        memberAnswer.getProblem().getEntry(),
                        memberAnswer.getProblem().getCategory(),
                        memberAnswer.getProblem().getType(),
                        memberAnswer.getProblem().getQuestion(),
                        memberAnswer.getProblem().getExplain(),
                        memberAnswer.getProblem().getChoice1(),
                        memberAnswer.getProblem().getChoice2(),
                        memberAnswer.getProblem().getChoice3(),
                        memberAnswer.getProblem().getChoice4(),
                        memberAnswer.getProblem().getChoice5(),
                        memberAnswer.getProblem().getAnswer(),
                        memberAnswer.getProblem().getAnswerExplain(),
                        memberAnswer.getProblem().getTotalAttempts(),
                        memberAnswer.getProblem().getIncorrectAttempts(),
                        memberAnswer.getProblem().getIncorrectRate(),
                        memberAnswer.getProblem().getDeletedAt()
                ),
                new ExamInformationDto(
                        memberAnswer.getExamInformation().getId(),
                        MemberDto.from(memberAnswer.getExamInformation().getMember()),
                        memberAnswer.getExamInformation().getEntry(),
                        memberAnswer.getExamInformation().getCategory(),
                        memberAnswer.getExamInformation().getType(),
                        memberAnswer.getExamInformation().getDate(),
                        memberAnswer.getExamInformation().getTime(),
                        memberAnswer.getExamInformation().getScore(),
                        memberAnswer.getExamInformation().getDeletedAt()
                ),
                memberAnswer.getChosenAnswer(),
                memberAnswer.getIsCorrect(),
                memberAnswer.getDeletedAt()
        );
    }
}
