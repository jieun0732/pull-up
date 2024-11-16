package pull_up.api.answer.dto;

import pull_up.infra.database.entity.Answer;

import java.util.List;

public record AnswerSolved(
        String entry,
        Boolean isSolvedEvenly,
        Integer answerTypeCount,
        List<AnswerType> answerTypes
) {
    public static AnswerSolved toDto(List<Answer> answers) {
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
