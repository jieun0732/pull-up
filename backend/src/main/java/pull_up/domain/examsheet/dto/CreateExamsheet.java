package pull_up.domain.examsheet.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record CreateExamsheet() {
    public record Request(
            String examTitle,
            List<ProblemSheet> problemSheets
    ) {
        public Map<Integer, Long> getProblemMap() {
            Map<Integer, Long> problemMap = new HashMap<>();
            for (ProblemSheet problemSheet : problemSheets) {
                problemMap.put(problemSheet.problemNumber(), problemSheet.problemId());
            }
            return problemMap;
        }
    }

    public record ProblemSheet(
            Integer problemNumber,
            Long problemId
    ) {
    }
}
