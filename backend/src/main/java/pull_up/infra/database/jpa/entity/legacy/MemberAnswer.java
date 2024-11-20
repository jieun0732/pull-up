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

@Getter
@Setter
@Entity
@Table(name = "member_answer_legacy")
@SQLRestriction("is_deleted = false")
public class MemberAnswer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberL memberL;

    @ManyToOne
    @JoinColumn(name = "problem_id")
    private ProblemL problemL;

    @ManyToOne
    @JoinColumn(name = "exam_information_id")
    private ExamL examL;

    @Column
    private String chosenAnswer;

    @Column
    private Boolean isCorrect;


    protected MemberAnswer() {
    }

    /**
     * 파라미터 생성자.
     */
    private MemberAnswer(MemberL memberL, ProblemL problemL, ExamL examL, String chosenAnswer, Boolean isCorrect) {
        this.memberL = memberL;
        this.problemL = problemL;
        this.examL = examL;
        this.chosenAnswer = chosenAnswer;
        this.isCorrect = isCorrect;
    }

    /**
     * 파라미터로부터 UserAnswer 엔티티 객체를 생성하는 함수.
     */
    public static MemberAnswer of(MemberL memberL, ProblemL problemL, ExamL examL, String chosenAnswer, Boolean isCorrect) {
        return new MemberAnswer(memberL, problemL, examL, chosenAnswer, isCorrect);
    }
}
