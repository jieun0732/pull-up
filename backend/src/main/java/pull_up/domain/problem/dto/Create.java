package pull_up.domain.problem.dto;

import lombok.extern.slf4j.Slf4j;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public record Create() {

    public record Request(
            Entry entry,
            String problemType,
            String question,
            String explanation,
            String choice1,
            String choice2,
            String choice3,
            String choice4,
            String choice5,
            String answer,
            String answerExplanation
    ) {
        public static Problem toEntity(Request createProblemReq) {
            return Problem.create(
                    createProblemReq.entry,
                    createProblemReq.problemType,
                    createProblemReq.question,
                    createProblemReq.explanation,
                    createProblemReq.choice1,
                    createProblemReq.choice2,
                    createProblemReq.choice3,
                    createProblemReq.choice4,
                    createProblemReq.choice5,
                    createProblemReq.answer,
                    createProblemReq.answerExplanation);
        }
    }

    public record FormatRequest() {
        public static List<Problem> toEntity(String formatString) {
            String[] formatCols = formatString.split("/row/");
            List<Problem> ret = new ArrayList<>();
            for (String formatCol : formatCols) {
                String[] values = formatCol.split("/col/");
                if (values.length != 11) throw new IllegalArgumentException("illegal column size");
                log.info("입력된 문제 : {}", Arrays.toString(values));
                ret.add(Problem.create(Entry.getEntry(values[0]), values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8], values[9], values[10]));
            }
            return ret;
        }
    }
}
