package pull_up.api.exam.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record GradeExam() {
    public record Request(
            Long examId,
            List<SelectedAnswer> selectedAnswers
    ) {
        public Map<Long, Integer> getSubmitMap() {
            Map<Long, Integer> ret = new HashMap<>(selectedAnswers.size());
            for (SelectedAnswer answer : selectedAnswers) {
                ret.put(answer.problemId(), answer.selectedAnswer());
            }
            return ret;
        }
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
