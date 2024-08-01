package pull_up.api.exam.dto;

import java.io.Serializable;
import pull_up.api.exam.entity.ExamProblem;
import pull_up.api.problem.dto.ProblemDto;

/**
 * DTO for {@link ExamProblem}
 */
public record ExamProblemDto(Long id, ExamInformationDto examInformation, ProblemDto problem, String chosenAnswer, Boolean isCorrect) implements Serializable {

    public static ExamProblemDto of(Long id, ExamInformationDto examInformation, ProblemDto problem,
        String chosenAnswer, Boolean isCorrect) {
        return new ExamProblemDto(id, examInformation, problem, chosenAnswer, isCorrect);
    }

    public static ExamProblemDto from(ExamProblem entity) {
        return new ExamProblemDto(
            entity.getId(),
            ExamInformationDto.from(entity.getExamInformation()),
            ProblemDto.from(entity.getProblem()),
            entity.getChosenAnswer(),
            entity.getIsCorrect()
        );
    }

    public static ExamProblem toEntity(ExamProblemDto dto) {
        return ExamProblem.of(
            ExamInformationDto.toEntity(dto.examInformation()),
            ProblemDto.toEntity(dto.problem()),
            dto.chosenAnswer(),
            dto.isCorrect()
        );
    }
}