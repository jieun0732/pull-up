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
import pull_up.api.member.entity.Member;

@Getter
@Setter
@Entity
@Table(name = "exam_information")
@SQLDelete(sql = "UPDATE exam_information e SET e.deleted_at = current_timestamp WHERE e.id = ?")
@SQLRestriction("deleted_at is NULL")
public class ExamInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Setter
    @Column
    private String category;

    @Setter
    @Column
    private LocalDateTime date;  // 문제를 푼 날짜

    @Setter
    @Column
    private LocalDateTime time;  // 문제 풀면서 걸린 소요시간

    @Setter
    @Column
    private Integer score;

    @Column
    private LocalDateTime deletedAt; // 삭제 여부

    protected ExamInformation() {
    }

    /**
     * 파라미터 생성자.
     */
    private ExamInformation(Member member, String category, LocalDateTime date, LocalDateTime time, Integer score) {
        this.member = member;
        this.category = category;
        this.date = date;
        this.time = time;
        this.score = score;
        this.deletedAt = null;
    }

    /**
     * 파라미터로부터 ExamInformation 엔티티 객체를 생성하는 함수.
     */
    public static ExamInformation of(Member member, String category, LocalDateTime date, LocalDateTime time, Integer score) {
        return new ExamInformation(member, category, date, time, score);
    }
}
