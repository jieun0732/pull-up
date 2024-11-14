package pull_up.api.exam.dto;

import java.util.List;

public record GradeExam() {
    public record Request(
            Long examId,
            List<SelectedAnswer> selectedAnswers
    ) {
    }

    public record Response(
            Integer totalCount,
            Integer correctCount,
            Integer incorrectCount,
            Double correctRate
    ) {
    }

    public record SelectedAnswer(
            Long problemId,
            Integer selectedAnswer
    ) {
    }
}
