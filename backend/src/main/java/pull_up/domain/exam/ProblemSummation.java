package pull_up.domain.exam;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.Answer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class ProblemSummation {
    private int totalProblemCount;
    private int solvedProblemCount;
    private int leftProblemCount;
    private int correctProblemCount;
    private int incorrectProblemCount;
    private Map<Entry, EntrySummation> entrySummations;

    public ProblemSummation(List<Answer> answers) {
        int solvedProblemCount = 0, correctProblemCount = 0;
        entrySummations = new HashMap<>();
        for (Entry entry : Entry.values()) entrySummations.put(entry,new EntrySummation(entry));
        for (Answer answer : answers) {
            EntrySummation entrySummation = entrySummations.get(answer.getProblem().getEntry());
            if (answer.getIsSubmitted()) {
                solvedProblemCount++;
                if (answer.getIsCorrect()) correctProblemCount++;
                entrySummation.add(answer.getProblem().getEntry(), answer.getIsCorrect());
            }
        }
        this.totalProblemCount = answers.size();
        this.solvedProblemCount = solvedProblemCount;
        this.leftProblemCount = totalProblemCount - solvedProblemCount;
        this.correctProblemCount = correctProblemCount;
        this.incorrectProblemCount = solvedProblemCount - correctProblemCount;
    }

    public static ProblemSummation createEmptyProblemSummation(int totalProblemCount) {
        return new ProblemSummation(totalProblemCount, 0,0,0,0, Map.of());
    }

    public List<Entry> getVulnerableEntry() {
        int maxIncorrect = 0;
        List<Entry> ret = new ArrayList<>();
        for (EntrySummation entrySummation : entrySummations.values()) {
            if (entrySummation.getIncorrectCount() > maxIncorrect) {
                maxIncorrect = entrySummation.getIncorrectCount();
                ret.clear();
                ret.add(entrySummation.entry);
            } else if (entrySummation.getIncorrectCount() == maxIncorrect) {
                ret.add(entrySummation.entry);
            }
        }
        return ret;
    }

    @Getter
    public static class EntrySummation {
        private final Entry entry;
        private int totalCount;
        private int correctCount;
        private int incorrectCount;

        public EntrySummation(Entry entry) {
            this.entry = entry;
        }

        void add(Entry entry, boolean isCorrect) {
            if (entry != this.entry) return;
            totalCount++;
            if (isCorrect) correctCount++;
            else incorrectCount++;
        }
    }
}
