package pull_up.domain.exam;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pull_up.infra.database.jpa.entity.Answer;

import java.util.List;

@Getter
@AllArgsConstructor
public class ProblemSummation {
    private Integer totalProblemCount;
    private Integer solvedProblemCount;
    private Integer leftProblemCount;
    private Integer correctProblemCount;
    private Integer incorrectProblemCount;

    public static ProblemSummation createProblemSummation(List<Answer> answers) {
        int solvedProblemCount = 0, correctProblemCount = 0;
        for (Answer answer : answers) {
            if (answer.getIsSubmitted()) {
                solvedProblemCount++;
                if (answer.getIsCorrect()) correctProblemCount++;
            }
        }
        int totalProblemCount = answers.size();
        int leftProblemCount = totalProblemCount - solvedProblemCount;
        int incorrectProblemCount = solvedProblemCount - correctProblemCount;
        return new ProblemSummation(totalProblemCount, solvedProblemCount, leftProblemCount, correctProblemCount, incorrectProblemCount);
    }

    public static ProblemSummation createEmptyProblemSummation(int totalProblemCount) {
        return new ProblemSummation(totalProblemCount, 0,0,0,0);
    }
}
