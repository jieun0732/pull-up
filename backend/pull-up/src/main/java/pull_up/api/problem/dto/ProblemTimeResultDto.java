package pull_up.api.problem.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import pull_up.api.problem.entity.Problem;

/**
 * DTO for {@link Problem} with choices as a list.
 */
public record ProblemTimeResultDto(
    Long id,
    String entry,
    String category,
    String type,
    String question,
    String explanation,
    List<String> choices,
    String answer,
    String answerExplain,
    Integer totalAttempts,
    Integer incorrectAttempts,
    Double incorrectRate,
    LocalDateTime createdDate // createdDate 필드 추가
) implements Serializable {

    public static ProblemTimeResultDto of(
        Long id,
        String entry,
        String category,
        String type,
        String question,
        String explanation,
        List<String> choices,
        String answer,
        String answerExplain,
        Integer totalAttempts,
        Integer incorrectAttempts,
        Double incorrectRate,
        LocalDateTime createdDate // createdDate 파라미터 추가
    ) {
        return new ProblemTimeResultDto(id, entry, category, type, question, explanation, choices,
            answer, answerExplain, totalAttempts, incorrectAttempts,
            incorrectRate, createdDate);
    }

    public static ProblemTimeResultDto from(Problem entity, LocalDateTime createdDate) { // createdDate 파라미터 추가
        List<String> choices = List.of(
            entity.getChoice1(),
            entity.getChoice2(),
            entity.getChoice3(),
            entity.getChoice4(),
            entity.getChoice5()
        );

        return new ProblemTimeResultDto(
            entity.getId(),
            entity.getEntry(),
            entity.getCategory(),
            entity.getType(),
            entity.getQuestion(),
            entity.getExplanation(),
            choices,
            entity.getAnswer(),
            entity.getAnswerExplain(),
            entity.getTotalAttempts(),
            entity.getIncorrectAttempts(),
            entity.getIncorrectRate(),
            createdDate // createdDate 값 설정
        );
    }
}