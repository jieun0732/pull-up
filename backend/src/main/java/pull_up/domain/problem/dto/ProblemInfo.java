package pull_up.domain.problem.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import pull_up.domain.problem.Entry;
import pull_up.global.util.GlobalFormatter;
import pull_up.infra.database.jpa.entity.Problem;

import java.time.LocalDateTime;

@Getter
public class ProblemInfo {
    private Long id;
    private String entry;
    private String problemType;
    private String createdDate;
    private String updatedDate;
    private String question;
    private Integer totalAttempts;
    private String correctRate;

    @QueryProjection
    public ProblemInfo(Long id, Entry entry, String problemType, LocalDateTime createdTime, LocalDateTime updatedTime, String questionSummary, Integer totalAttempts, Double correctRate) {
        if (questionSummary.length() > 30) questionSummary = questionSummary.substring(0, 30) + "...";
        this.id = id;
        this.entry = entry.getKorean();
        this.problemType = problemType;
        this.createdDate = createdTime.format(GlobalFormatter.KOREAN_DATE_FORMATTER);
        this.updatedDate = updatedTime.format(GlobalFormatter.KOREAN_DATE_FORMATTER);
        this.question = questionSummary;
        this.totalAttempts = totalAttempts;
        this.correctRate = String.format("%.0f", correctRate);
    }

    public static ProblemInfo toDto(Problem problem){
        return new ProblemInfo(problem.getId(),
                problem.getEntry(),
                problem.getProblemType(),
                problem.getCreatedTime(),
                problem.getUpdatedTime(),
                problem.getQuestionSummary(),
                problem.getTotalAttempts(),
                problem.getCorrectRate());
    }
}
