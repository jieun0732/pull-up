package pull_up.domain.exam.dto;

import pull_up.infra.database.jpa.entity.Answer;

public record Result(
        Integer problemNumber,
        String problemType,
        Boolean isSubmitted,
        Boolean isCorrect
) {
    public static Result toDto(Answer answer) {
        return new Result(
                answer.getProblemNumber(),
                answer.getProblem().getProblemType(),
                answer.getIsSubmitted(),
                answer.getIsCorrect());
    }
}
