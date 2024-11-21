package pull_up.infra.database.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Problemsheet {

    @Column(nullable = false)
    private Integer problemNumber;

    @Column(nullable = false)
    private Long problemId;

    public Problemsheet(Integer problemNumber, Long problemId) {
        this.problemNumber = problemNumber;
        this.problemId = problemId;
    }
}