package pull_up.api.exam.dto;

import pull_up.infra.database.entity.Answer;

import java.time.LocalDateTime;

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

        public static Brief toDto(Answer incorrectProblem) {
            return new Brief(incorrectProblem.getId(),
                    incorrectProblem.getProblemNumber(),
                    incorrectProblem.getProblem().getEntry(),
                    incorrectProblem.getProblem().getCategory(),
                    incorrectProblem.getProblem().getType(),
                    incorrectProblem.getProblem().getQuestion(),
                    incorrectProblem.getExam().getSolvedTime());
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
            String choice1,
            String choice2,
            String choice3,
            String choice4,
            String choice5,
            String chosenAnswer,
            String correctAnswer,
            String answerExplain,
            Double incorrectRate
    ) {

        public static Detail toDto(Answer incorrectAnswer) {
            return new Detail(incorrectAnswer.getId(),
                    incorrectAnswer.getProblemNumber(),
                    incorrectAnswer.getProblem().getEntry(),
                    incorrectAnswer.getProblem().getCategory(),
                    incorrectAnswer.getProblem().getType(),
                    incorrectAnswer.getProblem().getQuestion(),
                    incorrectAnswer.getProblem().getExplanation(),
                    incorrectAnswer.getProblem().getChoice1(),
                    incorrectAnswer.getProblem().getChoice2(),
                    incorrectAnswer.getProblem().getChoice3(),
                    incorrectAnswer.getProblem().getChoice4(),
                    incorrectAnswer.getProblem().getChoice5(),
                    incorrectAnswer.getChosenAnswer(),
                    incorrectAnswer.getProblem().getAnswer(),
                    incorrectAnswer.getProblem().getAnswerExplain(),
                    incorrectAnswer.getProblem().getIncorrectRate());
        }
    }
}
