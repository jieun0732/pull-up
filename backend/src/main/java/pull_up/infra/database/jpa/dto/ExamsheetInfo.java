package pull_up.infra.database.jpa.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pull_up.global.util.GlobalFormatter;

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

    @QueryProjection
    public ExamsheetInfo(Long id, LocalDateTime createdTime, LocalDateTime updatedTime, String examTitle, Integer problemCount, Integer examCount, Double averageScore, Duration averageDuration) {
        this.id = id;
        this.createdDate = createdTime.format(GlobalFormatter.KOREAN_DATE_FORMATTER);
        this.updatedDate = updatedTime.format(GlobalFormatter.KOREAN_DATE_FORMATTER);
        this.examTitle = examTitle;
        this.problemCount = problemCount;
        this.examCount = examCount;
        this.averageScore = String.format("%.1f", averageScore);
        this.averageDuration = averageDuration.toMinutesPart() + ":" + averageDuration.toSecondsPart();
    }
}
