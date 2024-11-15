package pull_up.api.answer.dto;

import pull_up.infra.database.entity.Answer;

public record AnswerDto(
        String chosenAnswer,
        String correctAnswer,
        String answerExplain,
        Boolean isCorrect,
        Double correctRate,
        Double inCorrectRate
) {
    public static AnswerDto toDto(Answer answer) {
        return new AnswerDto(answer.getChosenAnswer(),
                answer.getProblem().getAnswer(),
                answer.getProblem().getAnswerExplain(),
                answer.getIsCorrect(),
                answer.getProblem().getCorrectRate(),
                answer.getProblem().getIncorrectRate());
    }
}
