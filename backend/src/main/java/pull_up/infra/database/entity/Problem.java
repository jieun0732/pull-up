package pull_up.infra.database.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import pull_up.domain.problem.Entry;

import java.util.List;

@Entity
@Table(name = "problem")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Problem {

    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer totalAttempts;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer incorrectAttempts;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Double incorrectRate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Entry entry;

    @Column(nullable = false)
    private String problemType;

    @Lob
    @Column(nullable = false, columnDefinition = "BLOB")
    private byte[] question;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] example;

    @Column
    private String choice1;

    @Column
    private String choice2;

    @Column
    private String choice3;

    @Column
    private String choice4;

    @Column
    private String choice5;

    @Column(nullable = false)
    private String correctAnswer;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] explanation;

    public String getQuestionAsString() {
        return new String(question);
    }

    public String getExampleAsString() {
        return new String(example);
    }

    public String getExplanationAsString() {
        return new String(explanation);
    }

    public String getNormalProblemType() {
        return problemType.toUpperCase().replaceAll("\\s+", "");
    }
}
