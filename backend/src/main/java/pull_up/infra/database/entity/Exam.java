package pull_up.infra.database.entity;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import pull_up.global.entity.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "exam_information")
@SQLRestriction("is_deleted = false")
public class Exam extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "exam", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<AnsweredProblem> answeredProblem;

    @Column
    private String entry;

    @Column
    private String category;

    @Column
    private String type;

    @Column
    private LocalDateTime createdDate;  // 문제 생성 날짜

    @Column
    private LocalDateTime solvedTime; // 문제를 다 푼 시간

    @Column
    private Duration requiredTime;  // 문제 풀면서 걸린 소요시간

    @Column
    private Integer score;

    protected Exam() {
    }

    /**
     * 파라미터 생성자.
     */
    private Exam(Member member, String entry, String category, String type, LocalDateTime createdDate, LocalDateTime solvedTime, Duration requiredTime, Integer score) {
        this.member = member;
        this.entry = entry;
        this.category = category;
        this.type = type;
        this.createdDate = createdDate;
        this.solvedTime = solvedTime;
        this.requiredTime = requiredTime;
        this.score = score;
    }

    /**
     * 파라미터로부터 ExamInformation 엔티티 객체를 생성하는 함수.
     */
    public static Exam of(Member member, String entry, String category, String type, LocalDateTime createdDate, LocalDateTime solvedDate, Duration requiredTime, Integer score) {
        return new Exam(member, entry, category, type, createdDate, solvedDate, requiredTime, score);
    }

    public static Exam of(Member member, List<AnsweredProblem> answeredProblems, String entry, String category, String type, LocalDateTime createdDate, LocalDateTime solvedDate, Duration requiredTime, Integer score) {
        Exam exam = new Exam(member, entry, category, type, createdDate, solvedDate, requiredTime, score);
        exam.setAnsweredProblem(answeredProblems);
        return exam;
    }
}
