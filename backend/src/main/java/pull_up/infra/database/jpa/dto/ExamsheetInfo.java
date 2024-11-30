package pull_up.infra.database.jpa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pull_up.global.util.GlobalFormatter;
import pull_up.infra.database.jpa.entity.Examsheet;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ExamsheetInfo {
    private Long id;
    private String createdDate;
    private String updatedDate;
    private String examTitle;
    private Integer problemCount;
    private Integer examCount;
    private String averageScore;
    private String averageDuration;

    public ExamsheetInfo(Long id, LocalDateTime createdTime, LocalDateTime updatedTime, String examTitle, Integer problemCount, Integer examCount, Double averageScore, Duration averageDuration) {
        this.id = id;
        this.createdDate = createdTime.format(GlobalFormatter.KOREAN_DATE_FORMATTER);
        this.updatedDate = updatedTime.format(GlobalFormatter.KOREAN_DATE_FORMATTER);
        this.examTitle = examTitle;
        this.problemCount = problemCount;
        this.examCount = examCount;
        this.averageScore = String.format("%.1f", averageScore);
        this.averageDuration = averageDuration.toMinutesPart() + ":" + String.format("%02d", averageDuration.toSecondsPart());
    }

    public static ExamsheetInfo toDto(Examsheet e) {
        return new ExamsheetInfo(e.getId(),
                e.getCreatedTime(),
                e.getUpdatedTime(),
                e.getExamTitle(),
                e.getProblemsheets().size(),
                e.getExamCount(),
                e.getAverageScore(),
                e.getAverageDuration());
    }
}
