package pull_up.infra.database.jpa.fixture;

import lombok.RequiredArgsConstructor;
import pull_up.infra.database.jpa.entity.Answer;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public enum AnswerFixture implements Fixture<Answer> {
    INCORRECT_1(1L, false, true, 1, 1, "1", LocalDateTime.of(2024, 11, 16, 12, 56)),
    INCORRECT_2(2L, false, true, 2, 2, "3", LocalDateTime.of(2024, 11, 16, 12, 56)),
    INCORRECT_3(3L, false, true, 3, 1, "2", LocalDateTime.of(2024, 11, 16, 12, 56)),
    INCORRECT_4(4L, false, true, 4, 2, "4", LocalDateTime.of(2024, 11, 16, 12, 56)),
    INCORRECT_5(5L, false, true, 5, 1, "5", LocalDateTime.of(2024, 11, 16, 12, 56)),
    CORRECT_6(7L, true, true, 6, 1, "3", LocalDateTime.of(2024, 11, 16, 12, 56)),
    CORRECT_7(8L, true, true, 7, 3, "2", LocalDateTime.of(2024, 11, 16, 12, 56)),
    CORRECT_8(9L, true, true, 8, 1, "1", LocalDateTime.of(2024, 11, 16, 12, 56)),
    CORRECT_9(10L, true, true, 9, 2, "5", LocalDateTime.of(2024, 11, 16, 12, 56)),
    CORRECT_10(11L, true, true, 10, 1, "4", LocalDateTime.of(2024, 11, 16, 12, 56)),
    NOT_SUBMITTED_11(13L, null, false, 11, 0, null, LocalDateTime.of(2024, 11, 16, 12, 56)),
    NOT_SUBMITTED_12(14L, null, false, 12, 0, null, LocalDateTime.of(2024, 11, 16, 12, 56)),
    NOT_SUBMITTED_13(15L, null, false, 13, 0, null, LocalDateTime.of(2024, 11, 16, 12, 56)),
    NOT_SUBMITTED_14(16L, null, false, 14, 0, null, LocalDateTime.of(2024, 11, 16, 12, 56)),
    NOT_SUBMITTED_15(17L, null, false, 15, 0, null, LocalDateTime.of(2024, 11, 16, 12, 56));


    private final Long id;
    private final Boolean isCorrect;
    private final Boolean isSubmitted;
    private final Integer problemNumber;
    private final Integer submitCount;
    private final String submitAnswer;
    private final LocalDateTime submitTime;

    public Answer get() {
        return new Answer(id, isCorrect, isSubmitted, problemNumber, submitCount, submitAnswer, submitTime, null, null);
    }
}
