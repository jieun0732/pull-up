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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import pull_up.api.exam.entity.ExamInformation;
import pull_up.api.problem.entity.Problem;

@Getter
@Setter
@Entity
@Table(name = "user_answer")
@SQLDelete(sql = "UPDATE user_answer u SET u.deleted_at = current_timestamp WHERE u.id = ?")
@SQLRestriction("deleted_at is NULL")
public class MemberAnswer {

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
    @JoinColumn(name = "exam_id")
    private ExamInformation examInformation;

    @Setter
    @Column
    private String chosenAnswer;

    @Setter
    @Column
    private Boolean isCorrect;

    @Column
    private LocalDateTime deletedAt; // 삭제 여부

    protected MemberAnswer() {
    }

    /**
     * 파라미터 생성자.
     */
    private MemberAnswer(Member member, Problem problem, ExamInformation examInformation, String chosenAnswer, Boolean isCorrect) {
        this.member = member;
        this.problem = problem;
        this.examInformation = examInformation;
        this.chosenAnswer = chosenAnswer;
        this.isCorrect = isCorrect;
        this.deletedAt = null;
    }

    /**
     * 파라미터로부터 UserAnswer 엔티티 객체를 생성하는 함수.
     */
    public static MemberAnswer of(Member member, Problem problem, ExamInformation examInformation, String chosenAnswer, Boolean isCorrect) {
        return new MemberAnswer(member, problem, examInformation, chosenAnswer, isCorrect);
    }
}
