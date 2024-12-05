package pull_up.domain.exam.dto;

import pull_up.domain.exam.ExamType;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.Answer;
import pull_up.infra.database.jpa.entity.Exam;
import pull_up.infra.database.jpa.entity.Problem;

import java.util.List;

public record Start() {
    public record EvenlyRequest(
            Long memberId,
            Entry entry
    ) {}

    public record EvenlyRequestV2(
            Long memberId,
            String evenlyExamName,
            Entry entry
    ) {}

    public record ByProblemTypeRequest(
            Long memberId,
            Entry entry,
            String problemType
    ){}

    public record MockExamRequest(
            Long memberId,
            String mockExamName
    ) {
    }

    public record Response(
            Long examId,
            Integer totalProblemCount,
            Integer leftProblemCount,
            Integer problemNumber,
            Entry entry,
            String problemType,
            String question,
            String example,
            List<String> choices
    ) {

        public static Response toDto(Exam startedExam) {
            Answer answer1 = startedExam.getAnswers().get(0);
            Problem problem1 = answer1.getProblem();
            return new Response(
                    startedExam.getId(),
                    startedExam.getAnswers().size(),
                    startedExam.getAnswers().size() - 1,
                    answer1.getProblemNumber(),
                    problem1.getEntry(),
                    problem1.getProblemType(),
                    problem1.getQuestionAsString(),
                    problem1.getExampleAsString(),
                    List.of(problem1.getChoice1(), problem1.getChoice2(), problem1.getChoice3(), problem1.getChoice4(), problem1.getChoice5()));
        }
    }
}
