package pull_up.api.problem.dto;

import pull_up.infra.database.entity.Problem;

import java.util.ArrayList;
import java.util.List;

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

    public record FormatRequest(String formatString) {
        public static List<Problem> toEntity(String formatString) {
            String[] formatCols = formatString.split("/row/");
            List<Problem> ret = new ArrayList<>();
            for (String formatCol : formatCols) {
                String[] values = formatCol.split("/col/");
                ret.add(Problem.of(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], 0, 0, Double.parseDouble(values[12])));
            }
            return ret;
        }
    }
}
