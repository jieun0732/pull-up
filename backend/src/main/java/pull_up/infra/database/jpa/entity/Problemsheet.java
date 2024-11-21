package pull_up.infra.database.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Problemsheet {

    @Column(nullable = false)
    private Integer problemNumber;

    @Column(nullable = false)
    private Long problemId;
}