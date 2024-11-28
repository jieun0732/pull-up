package pull_up.domain.exam.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        public Map<Integer, Integer> getAnswerSheet() {
            Map<Integer, Integer> ret = new HashMap<>(answerSheets.size());
            for (AnswerSheet answerSheet : answerSheets) {
                ret.put(answerSheet.problemNumber(), answerSheet.submitAnswer());
            }
            return ret;
        }
    }
}
