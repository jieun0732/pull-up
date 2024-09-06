package pull_up.api.member.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import pull_up.api.exam.dto.ExamInformationDto;
import pull_up.api.member.entity.IncorrectAnswer;
import pull_up.api.problem.dto.ProblemResultDto;

/**
 * DTO for {@link IncorrectAnswer}
 */
public record IncorrectAnswerResultDto(Long id, MemberDto member, ProblemResultDto problem,
                                 ExamInformationDto examInformation, String chosenAnswer, LocalDateTime incorrectTime) implements
    Serializable {

    public static IncorrectAnswerResultDto of(Long id, MemberDto member, ProblemResultDto problem,
        ExamInformationDto examInformation, String chosenAnswer, LocalDateTime incorrectTime) {
        return new IncorrectAnswerResultDto(id, member, problem, examInformation, chosenAnswer, incorrectTime);
    }

    public static IncorrectAnswerResultDto from(IncorrectAnswer entity) {
        return new IncorrectAnswerResultDto(
            entity.getId(),
            entity.getMember() != null ? MemberDto.from(entity.getMember()) : null,
            entity.getProblem() != null ? ProblemResultDto.from(entity.getProblem()) : null,
            entity.getExamInformation() != null ? ExamInformationDto.from(entity.getExamInformation()) : null,
            entity.getChosenAnswer() != null ? entity.getChosenAnswer() : null,
            entity.getIncorrectTime() != null ? entity.getIncorrectTime() : null
        );
    }

}