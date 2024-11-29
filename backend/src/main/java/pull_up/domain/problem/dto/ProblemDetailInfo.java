package pull_up.domain.problem.dto;

import pull_up.domain.problem.Entry;
import pull_up.global.util.GlobalFormatter;
import pull_up.infra.database.jpa.entity.Problem;

import java.time.LocalDateTime;
import java.util.List;

public record ProblemDetailInfo(
        Long id,
        Entry entry,
        String problemType,
        Integer totalAttempts,
        String correctRate,
        String question,
        String example,
        List<String> choices,
        String correctAnswer,
        String explanation,
        String createdTime,
        String updatedTime
        ) {

    public static ProblemDetailInfo toDto(Problem problem) {
        return new ProblemDetailInfo(
                problem.getId(),
                problem.getEntry(),
                problem.getProblemType(),
                problem.getTotalAttempts(),
                String.format("%.1f", problem.getCorrectRate()),
                problem.getQuestionAsString(),
                problem.getExampleAsString(),
                List.of(problem.getChoice1(), problem.getChoice2(), problem.getChoice3(), problem.getChoice4(), problem.getChoice5()),
                problem.getCorrectAnswer(),
                problem.getExplanationAsString(),
                problem.getCreatedTime().format(GlobalFormatter.KOREAN_DATE_FORMATTER),
                problem.getUpdatedTime().format(GlobalFormatter.KOREAN_DATE_FORMATTER)
        );
    }
}
