package pull_up.domain.exam.dto;

import pull_up.infra.database.entity.Answer;

public record Submit() {
    public record Request(
            Long examId,
            Integer problemNumber,
            Integer submitAnswer
    ){

    }
    public record Response(
            Boolean isCorrect,
            Integer submitAnswer,
            Integer correctAnswer,
            Double correctRate,
            Double incorrectRate,
            String explanation
    ) {

        public static Response toDto(Answer answer) {
            return new Response(answer.getIsCorrect(),
                    answer.getSubmitAnswerToInt(),
                    answer.getProblem().getCorrectAnswerToInt(),
                    answer.getProblem().getCorrectRate(),
                    answer.getProblem().getIncorrectRate(),
                    answer.getProblem().getExplanationAsString());
        }
    }
}
