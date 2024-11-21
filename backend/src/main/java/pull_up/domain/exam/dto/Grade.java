package pull_up.domain.exam.dto;

import java.util.List;

public record Grade() {
    public record Request(
            Long examId,
            List<AnswerSheet> answerSheets
    ) {

        public record AnswerSheet(
                Integer problemNumber,
                Integer submitAnswer
        ) {
        }
    }

    public record Response(
    ) {}
}
