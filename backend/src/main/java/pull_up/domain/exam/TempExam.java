package pull_up.domain.exam;

import lombok.Getter;
import pull_up.infra.database.jpa.entity.Exam;

@Getter
public class TempExam extends Exam {

    private final ProblemSummation problemSummation;

    public TempExam(Integer totalProblemCount) {
        problemSummation = ProblemSummation.createEmptyProblemSummation(totalProblemCount);
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public ProblemSummation getProblemSummation() {
        return problemSummation;
    }
}
