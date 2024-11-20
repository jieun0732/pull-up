package pull_up.infra.database.jpa.entity.legacy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import pull_up.global.entity.BaseEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "exam_problem_legacy")
@SQLRestriction("is_deleted = false")
public class AnswerL extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exam_information_id")
    private ExamL examL;

    @ManyToOne
    @JoinColumn(name = "problem_id")
    private ProblemL problemL;

    @Column
    private Long problemNumber;

    @Column
    private Integer tryCount;

    @Column
    private String chosenAnswer;

    @Column
    private Boolean isCorrect;

    @Column
    private LocalDateTime solveTime;

    @Column
    private Boolean isSolved;

    protected AnswerL() {
    }

    /**
     * 파라미터 생성자.
     */
    private AnswerL(ExamL examL, ProblemL problemL, Long ProblemNumber, String chosenAnswer, Boolean isCorrect) {
        this.examL = examL;
        this.problemL = problemL;
        this.problemNumber = ProblemNumber;
        this.chosenAnswer = chosenAnswer;
        this.isCorrect = isCorrect;
        this.tryCount = 0;
        this.isSolved = false;
    }

    public static AnswerL of(ExamL examL, ProblemL problemL, Long ProblemNumber, String chosenAnswer, Boolean isCorrect) {
        return new AnswerL(examL, problemL, ProblemNumber, chosenAnswer, isCorrect);
    }

    public static AnswerL submit(ExamL examL, Long problemNumber, String selectedAnswer) {
        AnswerL answerL = examL.getAnswer(problemNumber);
        answerL.mark(Integer.valueOf(selectedAnswer));
        return answerL;
    }

    public void mark(Integer selectedAnswer) {
        chosenAnswer = selectedAnswer.toString();
        isCorrect = problemL.getAnswer().equals(chosenAnswer);
        problemL.addTotalAttempt(isCorrect);
        solveTime = LocalDateTime.now();
        tryCount++;
        isSolved = true;
    }
}
