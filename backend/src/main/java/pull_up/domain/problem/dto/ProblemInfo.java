package pull_up.domain.problem.dto;

import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.Problem;

public record ProblemInfo(
        Long id,
        Entry entry,
        String problemType,
        String question,
        Integer totalAttempts,
        String correctRate
) {
    public static ProblemInfo toDto(Problem problem) {
        String question = problem.getQuestionAsString();
        if (question.length() > 30) question = question.substring(0, 30) + "...";
        return new ProblemInfo(problem.getId(),
                problem.getEntry(),
                problem.getProblemType(),
                question,
                problem.getTotalAttempts(),
                String.format("%.0f", problem.getCorrectRate()));
    }
}
