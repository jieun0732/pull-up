package pull_up.api.problem.dto;

import pull_up.infra.database.entity.Problem;

public record CreateProblem() {

    public record Request(
            String entry, String category, String type, String question, String explanation, String choice1,
            String choice2,
            String choice3, String choice4, String choice5, String answer, String answerExplanation,
            Double incorrectRate
    ) {
        public static Problem toEntity(Request createProblemReq) {
            return Problem.of(createProblemReq.entry,
                    createProblemReq.category,
                    createProblemReq.type,
                    createProblemReq.question,
                    createProblemReq.explanation,
                    createProblemReq.choice1,
                    createProblemReq.choice2,
                    createProblemReq.choice3,
                    createProblemReq.choice4,
                    createProblemReq.choice5,
                    createProblemReq.answer,
                    createProblemReq.answerExplanation,
                    0,
                    0,
                    createProblemReq.incorrectRate);
        }
    }
}
