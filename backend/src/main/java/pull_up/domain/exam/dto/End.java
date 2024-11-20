package pull_up.domain.exam.dto;

import pull_up.domain.exam.ProblemSummation;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.Answer;
import pull_up.infra.database.jpa.entity.Exam;

import java.util.List;

public record End() {
    public record Request() {

    }

    public record Response(
            Entry entry,
            Boolean isFinished,
            String memberName,
            Integer totalProblemCount,
            Integer leftProblemCount,
            Integer correctProblemCount,
            Integer score,
            List<ProblemResult> problemResults
    ) {
        public record ProblemResult(
                Integer problemNumber,
                String problemType,
                Boolean isSubmitted,
                Boolean isCorrect
        ) {
            public static ProblemResult toDto(Answer answer) {
                return new ProblemResult(
                        answer.getProblemNumber(),
                        answer.getProblem().getProblemType(),
                        answer.getIsSubmitted(),
                        answer.getIsCorrect());
            }
        }

        public static Response toDto(Exam examInDB) {
            Answer firstAnswer = examInDB.getAnswers().get(0);
            ProblemSummation problemSummation = examInDB.getProblemSummation();
            return new Response(
                    firstAnswer.getProblem().getEntry(),
                    examInDB.getIsFinished(),
                    examInDB.getMember().getName(),
                    problemSummation.getTotalProblemCount(),
                    problemSummation.getLeftProblemCount(),
                    problemSummation.getCorrectProblemCount(),
                    examInDB.getScore(),
                    examInDB.getAnswers().stream().map(ProblemResult::toDto).toList());
        }
    }
}
