package pull_up.infra.database.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import pull_up.domain.problem.Entry;

import java.util.Map;

import static com.nimbusds.jose.util.StandardCharset.UTF_8;

@Entity
@Table(name = "problem")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Problem extends BaseEntity{

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

    @Column(nullable = false)
    private String choice1;

    @Column(nullable = false)
    private String choice2;

    @Column(nullable = false)
    private String choice3;

    @Column(nullable = false)
    private String choice4;

    @Column(nullable = false)
    private String choice5;

    @Column(nullable = false)
    private String correctAnswer;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] explanation;

    private Problem(Entry entry, String problemType, String question, String example, String choice1, String choice2, String choice3, String choice4, String choice5, String correctAnswer, String explanation) {
        this.entry = entry;
        this.problemType = problemType;
        this.question = question.getBytes(UTF_8);
        this.example = example.getBytes(UTF_8);
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.choice4 = choice4;
        this.choice5 = choice5;
        this.correctAnswer = correctAnswer;
        this.explanation = explanation.getBytes(UTF_8);
        this.totalAttempts = 0;
        this.incorrectAttempts = 0;
        this.incorrectRate = 0.0;
    }

    public static Problem create(Entry entry, String problemType, String question, String example, String choice1, String choice2, String choice3, String choice4, String choice5, String correctAnswer, String explanation) {
        return new Problem(entry, problemType, question, example, choice1, choice2, choice3, choice4, choice5, correctAnswer, explanation);
    }

    public static Problem create(Map<String, String> parameters) {
        Problem newProblem = new Problem(null, null, "", "", null, null, null, null, null, null, "");
        newProblem.map(parameters);
        return newProblem;
    }

    public void modify(Map<String, String> parameters) {
        map(parameters);
    }

    public String getQuestionAsString() {
        return new String(question);
    }

    public String getQuestionAsSubstring() {
        String questionAsString = getQuestionAsString();
        if (questionAsString.length() > 100) return questionAsString.substring(0, 100);
        return questionAsString;
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

    private void map(Map<String, String> parameters) {
        this.entry = Entry.getEntry(parameters.get("entry"));
        this.problemType = parameters.get("problemType");
        this.question = parameters.get("question").getBytes(UTF_8);
        this.example = parameters.get("example").getBytes(UTF_8);
        this.choice1 = parameters.get("choice1");
        this.choice2 = parameters.get("choice2");
        this.choice3 = parameters.get("choice3");
        this.choice4 = parameters.get("choice4");
        this.choice5 = parameters.get("choice5");
        this.correctAnswer = parameters.get("correctAnswer");
        this.explanation = parameters.get("explanation").getBytes(UTF_8);
    }
}
