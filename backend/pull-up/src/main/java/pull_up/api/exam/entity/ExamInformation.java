package pull_up.api.exam.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import pull_up.api.member.entity.Member;
import pull_up.global.common.entity.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "exam_information")
@SQLRestriction("is_deleted = false")
public class ExamInformation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Setter
    @Column
    private String entry;

    @Setter
    @Column
    private String category;

    @Setter
    @Column
    private String type;

    @Setter
    @Column
    private LocalDateTime createdDate;  // 문제 생성 날짜

    @Setter
    @Column
    private LocalDateTime solvedDate; // 문제를 다 푼 시간

    @Setter
    @Column
    private Duration requiredTime;  // 문제 풀면서 걸린 소요시간

    @Setter
    @Column
    private Integer score;


    protected ExamInformation() {
    }

    /**
     * 파라미터 생성자.
     */
    private ExamInformation(Member member, String entry, String category, String type, LocalDateTime createdDate, LocalDateTime solvedDate, Duration requiredTime, Integer score) {
        this.member = member;
        this.entry = entry;
        this.category = category;
        this.type = type;
        this.createdDate = createdDate;
        this.solvedDate = solvedDate;
        this.requiredTime = requiredTime;
        this.score = score;
    }

    /**
     * 파라미터로부터 ExamInformation 엔티티 객체를 생성하는 함수.
     */
    public static ExamInformation of(Member member, String entry, String category, String type, LocalDateTime createdDate, LocalDateTime solvedDate, Duration requiredTime, Integer score) {
        return new ExamInformation(member, entry, category, type, createdDate, solvedDate, requiredTime, score);
    }
}
