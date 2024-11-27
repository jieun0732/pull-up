package pull_up.domain.exam.dto;

import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.Answer;
import pull_up.infra.database.jpa.entity.Exam;
import pull_up.infra.database.jpa.entity.Problem;

import java.util.List;

public record Next() {
    public record Response(
            Long examId,
            Boolean isSubmitted,
            Explanation explanation,
            Integer totalProblemCount,
            Integer leftProblemCount,
            Integer problemNumber,
            Entry entry,
            String problemType,
            String question,
            String example,
            List<String> choices
    ) {
        public static Response toDto(Exam currentExam, Integer problemNumber) {
            Answer answer = currentExam.getAnswerByProblemNumber(problemNumber);
            Problem problem = answer.getProblem();
            return new Next.Response(
                    currentExam.getId(),
                    answer.getIsSubmitted(),
                    answer.getIsSubmitted()? Explanation.toDto(answer) : null,
                    currentExam.getAnswers().size(),
                    currentExam.getAnswers().size() - problemNumber,
                    answer.getProblemNumber(),
                    problem.getEntry(),
                    problem.getProblemType(),
                    problem.getQuestionAsString(),
                    problem.getExampleAsString(),
                    List.of(problem.getChoice1(), problem.getChoice2(), problem.getChoice3(), problem.getChoice4(), problem.getChoice5()));
        }
    }
}
