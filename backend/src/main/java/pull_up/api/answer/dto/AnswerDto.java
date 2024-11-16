package pull_up.api.answer.dto;

import pull_up.api.problem.dto.ProblemDto;
import pull_up.infra.database.entity.legacy.AnswerL;

public record AnswerDto(
        Integer chosenAnswer,
        Integer correctAnswer,
        String answerExplain,
        Boolean isCorrect,
        Double correctRate,
        Double inCorrectRate,
        ProblemDto problemDto
) {
    public static AnswerDto toDto(AnswerL answerL) {
        return new AnswerDto(
                Integer.parseInt(answerL.getChosenAnswer()),
                Integer.parseInt(answerL.getProblemL().getAnswer()),
                answerL.getProblemL().getAnswerExplain(),
                answerL.getIsCorrect(),
                answerL.getProblemL().getCorrectRate(),
                answerL.getProblemL().getIncorrectRate(),
                ProblemDto.toDto(answerL.getProblemL()));
    }
}
