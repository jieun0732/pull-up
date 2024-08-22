package pull_up.api.problem.dto;

import java.io.Serializable;
import java.util.List;
import pull_up.api.problem.entity.Problem;

/**
 * DTO for {@link Problem} with choices as a list.
 */
public record ProblemResultDto(
    Long id,
    String entry,
    String category,
    String type,
    String question,
    String explanation,
    List<String> choices, // choices as a list
    String answer,
    String answerExplain,
    Integer totalAttempts,
    Integer incorrectAttempts,
    Double incorrectRate
) implements Serializable {

    public static ProblemResultDto of(
        Long id,
        String entry,
        String category,
        String type,
        String question,
        String explanation,
        List<String> choices, // choices as a list
        String answer,
        String answerExplain,
        Integer totalAttempts,
        Integer incorrectAttempts,
        Double incorrectRate
    ) {
        return new ProblemResultDto(id, entry, category, type, question, explanation, choices,
            answer, answerExplain, totalAttempts, incorrectAttempts,
            incorrectRate);
    }

    public static ProblemResultDto from(Problem entity) {
        List<String> choices = List.of(
            entity.getChoice1(),
            entity.getChoice2(),
            entity.getChoice3(),
            entity.getChoice4(),
            entity.getChoice5()
        );

        return new ProblemResultDto(
            entity.getId(),
            entity.getEntry(),
            entity.getCategory(),
            entity.getType(),
            entity.getQuestion(),
            entity.getExplanation(),
            choices, // choices as a list
            entity.getAnswer(),
            entity.getAnswerExplain(),
            entity.getTotalAttempts(),
            entity.getIncorrectAttempts(),
            entity.getIncorrectRate()
        );
    }
}