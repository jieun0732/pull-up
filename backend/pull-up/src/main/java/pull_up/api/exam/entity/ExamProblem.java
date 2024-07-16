package pull_up.api.exam.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import pull_up.api.problem.entity.Problem;
import pull_up.global.common.entity.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "exam_problem")
@SQLRestriction("is_deleted = false")
public class ExamProblem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exam_information_id")
    private ExamInformation examInformation;

    @ManyToOne
    @JoinColumn(name = "problem_id")
    private Problem problem;


    protected ExamProblem() {
    }

    /**
     * 파라미터 생성자.
     */
    private ExamProblem(ExamInformation examInformation, Problem problem) {
        this.examInformation = examInformation;
        this.problem = problem;
    }

    /**
     * 파라미터로부터 ExamProblem 엔티티 객체를 생성하는 함수.
     */
    public static ExamProblem of(ExamInformation examInformation, Problem problem) {
        return new ExamProblem(examInformation, problem);
    }
}
