package pull_up.api.problem.dto;

import java.io.Serializable;

import pull_up.infra.database.entity.legacy.AnswerL;

public record ProblemSolvedDto(Long id, Long problemNumber, ProblemDto problem, String chosenAnswer, Boolean isCorrect) implements Serializable {

    public static ProblemSolvedDto of(Long id, Long problemNumber, ProblemDto problem, String chosenAnswer, Boolean isCorrect) {
        return new ProblemSolvedDto(id, problemNumber, problem, chosenAnswer, isCorrect);
    }

    public static ProblemSolvedDto from(AnswerL entity) {
        return new ProblemSolvedDto(
            entity.getId(),
            entity.getProblemNumber(),
            ProblemDto.toDto(entity.getProblemL()),
            entity.getChosenAnswer(),
            entity.getIsCorrect()
        );
    }
}
