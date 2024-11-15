package pull_up.api.answer.dto;

import pull_up.infra.database.entity.Answer;

public record Submit() {
    public record Request(
            Long examId,
            Long problemNumber,
            String selectedAnswer
    ) {

    }
    public record Response(
            String chosenAnswer,
            String correctAnswer,
            String answerExplain,
            Boolean isCorrect,
            Double correctRate,
            Double inCorrectRate
    ) {

        public static Response toDto(Answer answer) {
            return new Response(answer.getChosenAnswer(),answer.getProblem().getAnswer(), answer.getProblem().getAnswerExplain(), answer.getIsCorrect(), answer.getProblem().getCorrectRate(), answer.getProblem().getIncorrectRate());
        }
    }
}
