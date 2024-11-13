package pull_up.api.exam.dto;

import java.io.Serializable;

import pull_up.infra.database.entity.AnsweredProblem;
import pull_up.api.problem.dto.ProblemDto;

/**
 * DTO for {@link AnsweredProblem}
 */
public record ExamProblemDto(Long id, ExamInformationDto examInformation, ProblemDto problem, Long problemNumber, String chosenAnswer, Boolean isCorrect) implements Serializable {

    public static ExamProblemDto of(Long id, ExamInformationDto examInformation, ProblemDto problem,
        Long problemNumber, String chosenAnswer, Boolean isCorrect) {
        return new ExamProblemDto(id, examInformation, problem, problemNumber, chosenAnswer, isCorrect);
    }

    public static ExamProblemDto from(AnsweredProblem entity) {
        return new ExamProblemDto(
            entity.getId(),
            ExamInformationDto.from(entity.getExam()),
            ProblemDto.from(entity.getProblem()),
            entity.getProblemNumber(),
            entity.getChosenAnswer(),
            entity.getIsCorrect()
        );
    }

    public static AnsweredProblem toEntity(ExamProblemDto dto) {
        return AnsweredProblem.of(
            ExamInformationDto.toEntity(dto.examInformation()),
            ProblemDto.toEntity(dto.problem()),
            dto.problemNumber(),
            dto.chosenAnswer(),
            dto.isCorrect()
        );
    }
}