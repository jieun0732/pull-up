package pull_up.api.answer.dto;

import org.springframework.security.core.parameters.P;
import pull_up.api.problem.dto.ProblemDto;
import pull_up.infra.database.entity.Answer;

public record AnswerDto(
        Integer chosenAnswer,
        Integer correctAnswer,
        String answerExplain,
        Boolean isCorrect,
        Double correctRate,
        Double inCorrectRate,
        ProblemDto problemDto
) {
    public static AnswerDto toDto(Answer answer) {
        return new AnswerDto(
                Integer.parseInt(answer.getChosenAnswer()),
                Integer.parseInt(answer.getProblem().getAnswer()),
                answer.getProblem().getAnswerExplain(),
                answer.getIsCorrect(),
                answer.getProblem().getCorrectRate(),
                answer.getProblem().getIncorrectRate(),
                ProblemDto.toDto(answer.getProblem()));
    }
}
