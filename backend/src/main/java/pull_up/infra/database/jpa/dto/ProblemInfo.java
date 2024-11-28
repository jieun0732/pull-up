package pull_up.infra.database.jpa.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.Problem;

@Getter
public class ProblemInfo {
    private Long id;
    private Entry entry;
    private String problemType;
    private String question;
    private Integer totalAttempts;
    private String correctRate;

    @QueryProjection
    public ProblemInfo(Long id, Entry entry, String problemType, byte[] question, Integer totalAttempts, Double correctRate) {
        String questionString = new String(question);
        if (questionString.length() > 30) questionString = questionString.substring(0, 30) + "...";
        this.id = id;
        this.entry = entry;
        this.problemType = problemType;
        this.question = questionString;
        this.totalAttempts = totalAttempts;
        this.correctRate = String.format("%.0f", correctRate);
    }
}
