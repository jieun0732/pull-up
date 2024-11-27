package pull_up.domain.exam.dto;

import pull_up.infra.database.jpa.entity.Answer;

public record Explanation(
        Boolean isCorrect,
        Integer submitAnswer,
        Integer correctAnswer,
        Double correctRate,
        Double incorrectRate,
        String explanation
) {

    public static Explanation toDto(Answer answer) {
        return new Explanation(answer.getIsCorrect(),
                answer.getSubmitAnswerToInt(),
                answer.getProblem().getCorrectAnswerToInt(),
                answer.getProblem().getCorrectRate(),
                answer.getProblem().getIncorrectRate(),
                answer.getProblem().getExplanationAsString());
    }
}
