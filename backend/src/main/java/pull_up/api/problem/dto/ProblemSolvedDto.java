package pull_up.api.problem.dto;

import java.io.Serializable;
import pull_up.api.exam.entity.ExamProblem;

public record ProblemSolvedDto(Long id, Long problemNumber, ProblemDto problem, String chosenAnswer, Boolean isCorrect) implements Serializable {

    public static ProblemSolvedDto of(Long id, Long problemNumber, ProblemDto problem, String chosenAnswer, Boolean isCorrect) {
        return new ProblemSolvedDto(id, problemNumber, problem, chosenAnswer, isCorrect);
    }

    public static ProblemSolvedDto from(ExamProblem entity) {
        return new ProblemSolvedDto(
            entity.getId(),
            entity.getProblemNumber(),
            ProblemDto.from(entity.getProblem()),
            entity.getChosenAnswer(),
            entity.getIsCorrect()
        );
    }
}
