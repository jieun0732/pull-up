package pull_up.infra.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import pull_up.global.entity.BaseEntity;
import pull_up.infra.database.entity.legacy.IncorrectAnswer;
import pull_up.infra.database.entity.legacy.MemberAnswer;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "problem")
@SQLRestriction("is_deleted = false")
public class Problem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String entry; // 수리 언어 추리

    @Column
    private String category; // 모의고사 유형별 골고루

    @Column
    private String type; // 이외 분류

    @Lob
    @Column
    private String question;

    @Lob
    @Column
    private String explanation;

    @Column
    private String choice1;

    @Setter
    @Column
    private String choice2;

    @Column
    private String choice3;

    @Column
    private String choice4;

    @Column
    private String choice5;

    @Column
    private String answer;

    @Lob
    @Column
    private String answerExplain;

    @Column
    private Integer totalAttempts;

    @Setter
    @Column
    private Integer incorrectAttempts;

    @Column
    private Double incorrectRate;

    @OneToMany(mappedBy = "problem", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Answer> answers;

    @OneToMany(mappedBy = "problem", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<IncorrectAnswer> incorrectAnswers;

    @OneToMany(mappedBy = "problem", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<MemberAnswer> memberAnswers;

    protected Problem() {
    }

    /**
     * 파라미터 생성자.
     */
    private Problem(String entry, String category, String type, String question, String explanation,
        String choice1, String choice2, String choice3, String choice4, String choice5,
        String answer, String answerExplain, Integer totalAttempts, Integer incorrectAttempts,
        Double incorrectRate) {
        this.entry = entry;
        this.category = category;
        this.type = type;
        this.question = question;
        this.explanation = explanation;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.choice4 = choice4;
        this.choice5 = choice5;
        this.answer = answer;
        this.answerExplain = answerExplain;
        this.totalAttempts = totalAttempts;
        this.incorrectAttempts = incorrectAttempts;
        this.incorrectRate = incorrectRate;
    }

    /**
     * 파라미터로부터 Problem 엔티티 객체를 생성하는 함수.
     */
    public static Problem of(String entry, String category, String type, String question,
        String explanation, String choice1, String choice2, String choice3, String choice4,
        String choice5, String answer, String answerExplain, Integer totalAttempts,
        Integer incorrectAttempts, Double incorrectRate) {
        return new Problem(entry, category, type, question, explanation, choice1, choice2, choice3,
            choice4, choice5, answer, answerExplain, totalAttempts, incorrectAttempts,
            incorrectRate);
    }

    public void addTotalAttempt(Boolean isCorrect) {
        totalAttempts++;
        if (!isCorrect) incorrectAttempts++;
        incorrectRate = (double) incorrectAttempts / totalAttempts * 100;
    }
}