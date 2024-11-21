package pull_up.domain.examsheet.dto;

import java.util.List;

public record CreateExamsheet() {
    public record Request(
            List<ProblemSheet> problemSheets
    ) {

    }

    public record ProblemSheet(
            Integer problemNumber,
            Long problemId
    ) {

    }
}
