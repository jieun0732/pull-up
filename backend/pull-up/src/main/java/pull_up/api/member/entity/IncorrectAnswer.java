package pull_up.api.member.entity;

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
import org.hibernate.annotations.SQLRestriction;
import pull_up.api.exam.entity.ExamInformation;
import pull_up.api.problem.entity.Problem;
import pull_up.global.common.entity.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "incorrect_answer")
@SQLRestriction("is_deleted = false")
public class IncorrectAnswer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @ManyToOne
    @JoinColumn(name = "exam_information_id")
    private ExamInformation examInformation;

    @Setter
    @Column
    private String chosenAnswer;

    @Setter
    @Column
    private LocalDateTime incorrectTime;

    protected IncorrectAnswer() {
    }

    /**
     * 파라미터 생성자.
     */
    private IncorrectAnswer(Member member, Problem problem, ExamInformation examInformation, String chosenAnswer, LocalDateTime incorrectTime) {
        this.member = member;
        this.problem = problem;
        this.examInformation = examInformation;
        this.chosenAnswer = chosenAnswer;
        this.incorrectTime = incorrectTime;
    }

    /**
     * 파라미터로부터 UserAnswer 엔티티 객체를 생성하는 함수.
     */
    public static IncorrectAnswer of(Member member, Problem problem, ExamInformation examInformation, String chosenAnswer, LocalDateTime incorrectTime) {
        return new IncorrectAnswer(member, problem, examInformation, chosenAnswer, incorrectTime);
    }

}
