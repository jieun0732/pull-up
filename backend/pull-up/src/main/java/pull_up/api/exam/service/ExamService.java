package pull_up.api.exam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pull_up.api.exam.dto.ExamInformationDto;
import pull_up.api.exam.dto.ExamProblemDto;
import pull_up.api.exam.entity.ExamInformation;
import pull_up.api.exam.entity.ExamProblem;
import pull_up.api.exam.repository.ExamInformationRepository;
import pull_up.api.exam.repository.ExamProblemRepository;
import pull_up.api.member.dto.MemberDto;
import pull_up.api.member.entity.Member;
import pull_up.api.member.service.MemberService;
import pull_up.api.problem.dto.ProblemDto;
import pull_up.api.problem.entity.Problem;
import pull_up.api.problem.repository.ProblemRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 시험 관련 비즈니스 로직을 처리하는 서비스.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExamService {

    @Autowired
    private ExamInformationRepository examInformationRepository;

    @Autowired
    private ExamProblemRepository examProblemRepository;

    @Autowired
    private ProblemRepository problemRepository;


    @Transactional
    public ExamInformationDto createExamInformation(MemberDto memberDTO, String entry, String category, String type) {
        Member member = MemberDto.toEntity(memberDTO);
        List<Problem> problems = problemRepository.findByEntryAndCategoryAndType(entry, category, type);
        ExamInformation examInformation = ExamInformation.of(member, entry, category, type, null, null, null, 0);
        examInformationRepository.save(examInformation);

        for (Problem problem : problems) {
            ExamProblem examProblem = ExamProblem.of(examInformation, problem);
            examProblemRepository.save(examProblem);
        }

        return toDTO(examInformation);
    }

    public List<ExamProblemDto> getExamProblems(Long examId) {
        List<ExamProblem> examProblems = examProblemRepository.findByExamInformationId(examId);
        return examProblems.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ExamInformationDto> getAllExamInformation() {
        return examInformationRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ExamInformationDto getExamInformation(Long examId) {
        return examInformationRepository.findById(examId).map(this::toDTO).orElse(null);
    }

    private ExamInformationDto toDTO(ExamInformation examInformation) {
        return new ExamInformationDto(
                examInformation.getId(),
                MemberDto.from(examInformation.getMember()),
                examInformation.getEntry(),
                examInformation.getCategory(),
                examInformation.getType(),
                examInformation.getCreatedDate(),
                examInformation.getSolvedDate(),
                examInformation.getRequiredTime(),
                examInformation.getScore()
        );
    }

    private ExamProblemDto toDTO(ExamProblem examProblem) {
        return new ExamProblemDto(
                examProblem.getId(),
                toDTO(examProblem.getExamInformation()),
                new ProblemDto(
                        examProblem.getProblem().getId(),
                        examProblem.getProblem().getEntry(),
                        examProblem.getProblem().getCategory(),
                        examProblem.getProblem().getType(),
                        examProblem.getProblem().getQuestion(),
                        examProblem.getProblem().getExplanation(),
                        examProblem.getProblem().getChoice1(),
                        examProblem.getProblem().getChoice2(),
                        examProblem.getProblem().getChoice3(),
                        examProblem.getProblem().getChoice4(),
                        examProblem.getProblem().getChoice5(),
                        examProblem.getProblem().getAnswer(),
                        examProblem.getProblem().getAnswerExplain(),
                        examProblem.getProblem().getTotalAttempts(),
                        examProblem.getProblem().getIncorrectAttempts(),
                        examProblem.getProblem().getIncorrectRate()
                )
        );
    }
}
