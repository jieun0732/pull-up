package pull_up.domain.examsheet.dto;

import pull_up.global.util.GlobalFormatter;
import pull_up.infra.database.jpa.entity.Examsheet;
import pull_up.infra.database.jpa.entity.Problem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record ExamsheetDetailInfo(
        Long id,
        String createdDate,
        String updatedDate,
        String examTitle,
        Integer examCount,
        String averageScore,
        String averageDuration,
        List<ProblemInfo> problemInfos
) {
    public static ExamsheetDetailInfo toDto(Examsheet examsheet, Map<Integer, Problem> problemMap) {
        List<ProblemInfo> problemInfoList = new ArrayList<>();
        for (Map.Entry<Integer, Problem> entry : problemMap.entrySet())
            problemInfoList.add(ProblemInfo.toDto(entry.getKey(), entry.getValue()));

        return new ExamsheetDetailInfo(examsheet.getId(),
                examsheet.getCreatedTime().format(GlobalFormatter.KOREAN_DATE_FORMATTER),
                examsheet.getUpdatedTime().format(GlobalFormatter.KOREAN_DATE_FORMATTER),
                examsheet.getExamTitle(),
                examsheet.getExamCount(),
                String.format("%.1f", examsheet.getAverageScore()),
                examsheet.getAverageDuration().toMinutesPart() + ":" + String.format("%02d", examsheet.getAverageDuration().toSecondsPart()),
                problemInfoList);
    }

    public record ProblemInfo(
            Integer problemNumber,
            Long problemId,
            String entry,
            String problemType,
            String question
    ) {
        public static ProblemInfo toDto(Integer problemNumber, Problem problem) {
            String questionSummary = problem.getQuestionSummary();
            if (questionSummary.length() > 70) questionSummary = questionSummary.substring(0, 70) + "...";
            return new ProblemInfo(problemNumber,
                    problem.getId(),
                    problem.getEntry().getKorean(),
                    problem.getProblemType(),
                    questionSummary);
        }
    }
}
