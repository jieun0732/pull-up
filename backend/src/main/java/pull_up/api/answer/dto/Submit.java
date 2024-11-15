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
            AnswerDto answer
    ) {
    }
}
