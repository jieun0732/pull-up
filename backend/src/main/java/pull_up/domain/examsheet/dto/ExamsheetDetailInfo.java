package pull_up.domain.examsheet.dto;

import pull_up.global.util.GlobalFormatter;
import pull_up.infra.database.jpa.dto.ProblemInfo;
import pull_up.infra.database.jpa.entity.Examsheet;
import pull_up.infra.database.jpa.entity.Problem;

import java.util.List;

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
    public static ExamsheetDetailInfo toDto(Examsheet examsheet, List<Problem> problems) {
        return new ExamsheetDetailInfo(examsheet.getId(),
                examsheet.getCreatedTime().format(GlobalFormatter.KOREAN_DATE_FORMATTER),
                examsheet.getUpdatedTime().format(GlobalFormatter.KOREAN_DATE_FORMATTER),
                examsheet.getExamTitle(),
                examsheet.getExamCount(),
                String.format("%.1f", examsheet.getAverageScore()),
                examsheet.getAverageDuration().toMinutesPart() + ":" + String.format("%02d", examsheet.getAverageDuration().toSecondsPart()),
                problems.stream().map(ProblemInfo::toDto).toList());
    }
}
