package pull_up.api.problem.dto;

import java.io.Serializable;

import pull_up.infra.database.entity.Answer;

public record ProblemSolvedDto(Long id, Long problemNumber, ProblemDto problem, String chosenAnswer, Boolean isCorrect) implements Serializable {

    public static ProblemSolvedDto of(Long id, Long problemNumber, ProblemDto problem, String chosenAnswer, Boolean isCorrect) {
        return new ProblemSolvedDto(id, problemNumber, problem, chosenAnswer, isCorrect);
    }

    public static ProblemSolvedDto from(Answer entity) {
        return new ProblemSolvedDto(
            entity.getId(),
            entity.getProblemNumber(),
            ProblemDto.toDto(entity.getProblem()),
            entity.getChosenAnswer(),
            entity.getIsCorrect()
        );
    }
}
