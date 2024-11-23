package pull_up.infra.database.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "answer")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Answer extends BaseEntity {

    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Boolean isCorrect;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Boolean isSubmitted;

    @Column(nullable = false)
    private Integer problemNumber;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer submitCount;

    @Column
    private String submitAnswer;

    @Column
    private LocalDateTime submitTime;

    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    private Answer(Exam exam, Problem problem, Integer problemNumber) {
        this.isSubmitted = false;
        this.submitCount = 0;
        this.problemNumber = problemNumber;
        this.exam = exam;
        this.problem = problem;
    }

    public static Answer makeEmptyAnswer(Exam exam, Problem problem, Integer problemNumber) {
        return new Answer(exam, problem, problemNumber);
    }

    public Integer getSubmitAnswerToInt() {
        return submitAnswer == null ? null : Integer.parseInt(submitAnswer);
    }

    public boolean mark(Integer submitAnswer) {
        String submitAnswerToString = submitAnswer == null? "0" : Integer.toString(submitAnswer);
        this.submitAnswer = submitAnswerToString;
        isCorrect = problem.getCorrectAnswer().equals(submitAnswerToString);
        problem.addTotalAttempt(isCorrect);
        submitTime = LocalDateTime.now();
        isSubmitted = true;
        submitCount++;

        return isCorrect;
    }

    public void reset() {
        isSubmitted = false;
        isCorrect = null;
        submitTime = null;
        submitAnswer = null;
        submitCount = 0;
    }
}
