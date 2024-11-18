package pull_up.domain.exam;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProblemSummation {
    private Integer totalProblemCount;
    private Integer leftProblemCount;
    private Integer correctProblemCount;
}
