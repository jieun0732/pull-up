package pull_up.api.answer.dto;

public record AnswerSubmit() {
    public record Request(
            Long examId,
            Long problemNumber,
            String selectedAnswer
    ) {

    }
    public record Response(
            AnswerDto answer
    ) {
    }
}
