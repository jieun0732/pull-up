package pull_up.api.member.dto;

import java.io.Serializable;
import java.util.List;
import pull_up.api.exam.dto.ExamInformationDto;
import pull_up.api.member.entity.IncorrectAnswer;
import pull_up.api.problem.dto.ProblemDto;

/**
 * DTO for {@link IncorrectAnswer}
 */
public record IncorrectAnswerDto(Long id, MemberDto member, ProblemDto problem,
                                 ExamInformationDto examInformation, String chosenAnswer) implements
    Serializable {

    public static IncorrectAnswerDto of(Long id, MemberDto member, ProblemDto problem,
        ExamInformationDto examInformation, String chosenAnswer, Boolean isCorrect) {
        return new IncorrectAnswerDto(id, member, problem, examInformation, chosenAnswer);
    }

    public static IncorrectAnswerDto from(IncorrectAnswer entity) {
        return new IncorrectAnswerDto(entity.getId(), MemberDto.from(entity.getMember()),
            ProblemDto.from(entity.getProblem()),
            ExamInformationDto.from(entity.getExamInformation()),
            entity.getChosenAnswer());
    }

    public static IncorrectAnswer toEntity(IncorrectAnswerDto dto) {
        return IncorrectAnswer.of(MemberDto.toEntity(dto.member()),
            ProblemDto.toEntity(dto.problem()),
            ExamInformationDto.toEntity(dto.examInformation()), dto.chosenAnswer());
    }
}