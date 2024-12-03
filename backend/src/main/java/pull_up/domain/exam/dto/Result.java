package pull_up.domain.exam.dto;

import pull_up.domain.exam.ProblemSummation;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.Answer;
import pull_up.infra.database.jpa.entity.Exam;

import java.util.List;

public record Result() {
    public record ByEntryResponse(
            Entry entry,
            Boolean isFinished,
            String memberName,
            Integer totalProblemCount,
            Integer leftProblemCount,
            Integer correctProblemCount,
            Integer score,
            List<ResultInfo> results
    ) {

        public static ByEntryResponse toDto(Exam examInDB) {
            Answer firstAnswer = examInDB.getAnswers().get(0);
            ProblemSummation problemSummation = examInDB.getProblemSummation();
            return new ByEntryResponse(
                    firstAnswer.getProblem().getEntry(),
                    examInDB.getIsFinished(),
                    examInDB.getMember().getName(),
                    problemSummation.getTotalProblemCount(),
                    problemSummation.getLeftProblemCount(),
                    problemSummation.getCorrectProblemCount(),
                    examInDB.getScore(),
                    examInDB.getAnswers().stream().map(ResultInfo::toDto).toList());
        }
    }

    public record MockExamResponse(
            String memberName,
            Integer totalProblemCount,
            Integer correctProblemCount,
            Integer score,
            Long durationSecond,
            List<ResultInfo> results
    ) {
        public static MockExamResponse toDto(Exam examInDB) {
            ProblemSummation problemSummation = examInDB.getProblemSummation();
            return new MockExamResponse(
                    examInDB.getMember().getName(),
                    problemSummation.getTotalProblemCount(),
                    problemSummation.getCorrectProblemCount(),
                    examInDB.getScore(),
                    examInDB.getDuration().toSeconds(),
                    examInDB.getAnswers().stream().map(ResultInfo::toDto).toList());
        }
    }

    public record ResultInfo(
            Integer problemNumber,
            String problemType,
            Boolean isSubmitted,
            Boolean isCorrect
    ) {
        public static ResultInfo toDto(Answer answer) {
            return new ResultInfo(
                    answer.getProblemNumber(),
                    answer.getProblem().getProblemType(),
                    answer.getIsSubmitted(),
                    answer.getIsCorrect());
        }
    }
}
