package pull_up.api.exam.dto;

import pull_up.infra.database.jpa.entity.legacy.AnswerL;

import java.time.LocalDateTime;
import java.util.List;

public record IncorrectAnswer() {
    public record Brief(
            Long answerId,
            Long ProblemNumber,
            String entry,
            String category,
            String type,
            String question,
            LocalDateTime solvedTime
    ) {

        public static Brief toDto(AnswerL incorrectProblem) {
            return new Brief(incorrectProblem.getId(),
                    incorrectProblem.getProblemNumber(),
                    incorrectProblem.getProblemL().getEntry(),
                    incorrectProblem.getProblemL().getCategory(),
                    incorrectProblem.getProblemL().getType(),
                    incorrectProblem.getProblemL().getQuestion(),
                    incorrectProblem.getExamL().getSolvedTime());
        }
    }

    public record Detail(
            Long answerId,
            Long problemNumber,
            String entry,
            String category,
            String type,
            String question,
            String explanation,
            List<String> choices,
            String chosenAnswer,
            String correctAnswer,
            String answerExplain,
            Double incorrectRate
    ) {

        public static Detail toDto(AnswerL incorrectAnswerL) {
            return new Detail(incorrectAnswerL.getId(),
                    incorrectAnswerL.getProblemNumber(),
                    incorrectAnswerL.getProblemL().getEntry(),
                    incorrectAnswerL.getProblemL().getCategory(),
                    incorrectAnswerL.getProblemL().getType(),
                    incorrectAnswerL.getProblemL().getQuestion(),
                    incorrectAnswerL.getProblemL().getExplanation(),
                    List.of(
                            incorrectAnswerL.getProblemL().getChoice1(),
                            incorrectAnswerL.getProblemL().getChoice2(),
                            incorrectAnswerL.getProblemL().getChoice3(),
                            incorrectAnswerL.getProblemL().getChoice4(),
                            incorrectAnswerL.getProblemL().getChoice5()),
                    incorrectAnswerL.getChosenAnswer(),
                    incorrectAnswerL.getProblemL().getAnswer(),
                    incorrectAnswerL.getProblemL().getAnswerExplain(),
                    incorrectAnswerL.getProblemL().getIncorrectRate());
        }
    }
}
