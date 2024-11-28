package pull_up.domain.exam;

import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.Answer;

import java.util.List;

public class ProblemDetailSummation extends ProblemSummation {
    private List<EntrySummation<Entry>> entrySummations;

    public ProblemDetailSummation(List<Answer> answers) {
        super(answers);

    }

    public static class EntrySummation<T extends Entry> {
        private T entry;
        private Integer totalCount;
        private Integer incorrectCount;
    }
}
