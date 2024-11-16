package pull_up.api.answer.dto;

import pull_up.infra.database.entity.legacy.AnswerL;

import java.util.List;

public record AnswerSolved(
        String entry,
        Boolean isSolvedEvenly,
        Integer answerTypeCount,
        List<AnswerType> answerTypes
) {
    public static AnswerSolved toDto(List<AnswerL> answerLS) {
        return null;
    }

    public record AnswerType(
            String type,
            Integer solvedProblemCount,
            Integer totalProblemCount,
            Boolean isSolved
    ) {

    }
}
