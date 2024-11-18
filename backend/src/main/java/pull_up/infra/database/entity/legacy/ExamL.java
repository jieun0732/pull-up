package pull_up.infra.database.entity.legacy;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import pull_up.global.entity.BaseEntity;
import pull_up.domain.answer.exception.AnswerErrorCode;
import pull_up.domain.answer.exception.AnswerException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "exam_information_legacy")
@SQLRestriction("is_deleted = false")
public class ExamL extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberL memberL;

    @OneToMany(mappedBy = "examL", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<AnswerL> answerLS;

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

    protected ExamL() {
    }

    /**
     * 파라미터 생성자.
     */
    private ExamL(MemberL memberL, String entry, String category, String type, LocalDateTime createdDate, LocalDateTime solvedTime, Duration requiredTime, Integer score) {
        this.memberL = memberL;
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
    public static ExamL of(MemberL memberL, String entry, String category, String type, LocalDateTime createdDate, LocalDateTime solvedDate, Duration requiredTime, Integer score) {
        return new ExamL(memberL, entry, category, type, createdDate, solvedDate, requiredTime, score);
    }

    public static ExamL of(MemberL memberL, List<AnswerL> answerLS, String entry, String category, String type, LocalDateTime createdDate, LocalDateTime solvedDate, Duration requiredTime, Integer score) {
        ExamL examL = new ExamL(memberL, entry, category, type, createdDate, solvedDate, requiredTime, score);
        examL.setAnswerLS(answerLS);
        return examL;
    }

    public int[] grade(Map<Long, Integer> submit) {
        int correctCount = 0, incorrectCount = 0;
        for (Map.Entry<Long, Integer> s : submit.entrySet()) {
            AnswerL answerL = getAnswer(s.getKey());
            answerL.mark(s.getValue());

            if (answerL.getIsCorrect()) correctCount++;
            else incorrectCount++;
        }

        solvedTime = LocalDateTime.now();
        return new int[]{correctCount, incorrectCount};
    }

    public AnswerL getAnswer(Long problemNumber) {
        for (AnswerL answerL : answerLS)
            if (answerL.getProblemNumber().equals(problemNumber)) return answerL;

        throw new AnswerException(AnswerErrorCode.NOT_FOUND);
    }
}
