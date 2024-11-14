package pull_up.infra.database.entity;

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

@Getter
@Setter
@Entity
@Table(name = "exam_problem")
@SQLRestriction("is_deleted = false")
public class Answer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exam_information_id")
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @Column
    private Long problemNumber;

    @Column
    private String chosenAnswer;

    @Column
    private Boolean isCorrect;

    protected Answer() {
    }

    /**
     * 파라미터 생성자.
     */
    private Answer(Exam exam, Problem problem, Long ProblemNumber, String chosenAnswer, Boolean isCorrect) {
        this.exam = exam;
        this.problem = problem;
        this.problemNumber = ProblemNumber;
        this.chosenAnswer = chosenAnswer;
        this.isCorrect = isCorrect;
    }

    /**
     * 파라미터로부터 ExamProblem 엔티티 객체를 생성하는 함수.
     */
    public static Answer of(Exam exam, Problem problem, Long ProblemNumber, String chosenAnswer, Boolean isCorrect) {
        return new Answer(exam, problem, ProblemNumber, chosenAnswer, isCorrect);
    }

    public void grade(Integer selectedAnswer) {
        this.chosenAnswer = selectedAnswer.toString();
        if (problem.getAnswer().equals(chosenAnswer)) isCorrect = true;
        problem.addTotalAttempt(isCorrect);
    }
}
