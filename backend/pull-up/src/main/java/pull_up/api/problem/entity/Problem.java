package pull_up.api.problem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import pull_up.global.common.entity.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "problem")
@SQLRestriction("is_deleted = false")
public class Problem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column
    private String entry;

    @Setter
    @Column
    private String category;

    @Setter
    @Column
    private String type;

    @Lob
    @Setter
    @Column
    private String question;

    @Lob
    @Setter
    @Column
    private String explanation;

    @Setter
    @Column
    private String choice1;

    @Setter
    @Column
    private String choice2;

    @Setter
    @Column
    private String choice3;

    @Setter
    @Column
    private String choice4;

    @Setter
    @Column
    private String choice5;

    @Setter
    @Column
    private String answer;

    @Lob
    @Setter
    @Column
    private String answerExplain;

    @Setter
    @Column
    private Integer totalAttempts;

    @Setter
    @Column
    private Integer incorrectAttempts;

    @Setter
    @Column
    private Double incorrectRate;


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
}