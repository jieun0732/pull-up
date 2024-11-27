package pull_up.domain.exam.dto;

import pull_up.domain.exam.ProblemSummation;
import pull_up.infra.database.jpa.entity.Exam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record Grade() {
    public record Request(
            Long examId,
            List<AnswerSheet> answerSheets
    ) {

        public record AnswerSheet(
                Integer problemNumber,
                Integer submitAnswer
        ) {
        }

        public Map<Integer, Integer> getAnswerSheet() {
            Map<Integer, Integer> ret = new HashMap<>(answerSheets.size());
            for (AnswerSheet answerSheet : answerSheets) {
                ret.put(answerSheet.problemNumber(), answerSheet.submitAnswer());
            }
            return ret;
        }
    }

    public record Response(
            String memberName,
            Integer totalProblemCount,
            Integer correctProblemCount,
            Integer score,
            Long durationSecond,
            List<Result> results
    ) {
        public static Response toDto(Exam examInDB) {
            ProblemSummation problemSummation = examInDB.getProblemSummation();
            return new Response(
                    examInDB.getMember().getName(),
                    problemSummation.getTotalProblemCount(),
                    problemSummation.getCorrectProblemCount(),
                    examInDB.getScore(),
                    examInDB.getDuration().toSeconds(),
                    examInDB.getAnswers().stream().map(Result::toDto).toList());
        }
    }
}
