package pull_up.api.problem.dto;

import pull_up.infra.database.jpa.entity.legacy.ProblemL;

import java.util.List;

public record ProblemDto(
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
        Double incorrectRate
) {

    public static ProblemDto toDto(ProblemL entity) {
        return new ProblemDto(entity.getId(),
                entity.getEntry(),
                entity.getCategory(),
                entity.getType(),
                entity.getQuestion(),
                entity.getExplanation(),
                List.of(entity.getChoice1(), entity.getChoice2(), entity.getChoice3(), entity.getChoice4(), entity.getChoice5()),
                entity.getAnswer(),
                entity.getAnswerExplain(),
                entity.getTotalAttempts(),
                entity.getIncorrectAttempts(),
                entity.getIncorrectRate());
    }

    public static ProblemL toEntity(ProblemDto dto) {
        return ProblemL.of(dto.entry(), dto.category(), dto.type(), dto.question(),
                dto.explanation(), dto.choices().get(0), dto.choices().get(1), dto.choices().get(2), dto.choices().get(3),
                dto.choices().get(4), dto.answer(), dto.answerExplain(), dto.totalAttempts(),
                dto.incorrectAttempts(), dto.incorrectRate());
    }

}