package pull_up.infra.database.entity;

import com.nimbusds.jose.util.StandardCharset;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import pull_up.domain.problem.Entry;

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

    private Problem(Entry entry, String problemType, String question, String example, String choice1, String choice2, String choice3, String choice4, String choice5, String correctAnswer, String explanation) {
        this.entry = entry;
        this.problemType = problemType;
        this.question = question.getBytes(StandardCharset.UTF_8);
        this.example = example.getBytes(StandardCharset.UTF_8);
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.choice4 = choice4;
        this.choice5 = choice5;
        this.correctAnswer = correctAnswer;
        this.explanation = explanation.getBytes(StandardCharset.UTF_8);
    }

    public static Problem createProblem(Entry entry, String problemType, String question, String example, String choice1, String choice2, String choice3, String choice4, String choice5, String correctAnswer, String explanation) {
        return new Problem(entry, problemType, question, example, choice1, choice2, choice3, choice4, choice5, correctAnswer, explanation);
    }

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

    public Integer getCorrectAnswerToInt() {
        return correctAnswer.isEmpty() ? null : Integer.parseInt(correctAnswer);
    }

    public Double getCorrectRate() {
        return 100 - getIncorrectRate();
    }

    public void addTotalAttempt(Boolean isCorrect) {
        totalAttempts++;
        if (!isCorrect) incorrectAttempts++;
        incorrectRate = (double) incorrectAttempts / totalAttempts * 100;
    }
}
