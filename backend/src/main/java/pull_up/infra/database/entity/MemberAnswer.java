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
@Table(name = "member_answer")
@SQLRestriction("is_deleted = false")
public class MemberAnswer extends BaseEntity {

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
    private Exam exam;

    @Column
    private String chosenAnswer;

    @Column
    private Boolean isCorrect;


    protected MemberAnswer() {
    }

    /**
     * 파라미터 생성자.
     */
    private MemberAnswer(Member member, Problem problem, Exam exam, String chosenAnswer, Boolean isCorrect) {
        this.member = member;
        this.problem = problem;
        this.exam = exam;
        this.chosenAnswer = chosenAnswer;
        this.isCorrect = isCorrect;
    }

    /**
     * 파라미터로부터 UserAnswer 엔티티 객체를 생성하는 함수.
     */
    public static MemberAnswer of(Member member, Problem problem, Exam exam, String chosenAnswer, Boolean isCorrect) {
        return new MemberAnswer(member, problem, exam, chosenAnswer, isCorrect);
    }
}
