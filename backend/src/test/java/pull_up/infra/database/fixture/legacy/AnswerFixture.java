package pull_up.infra.database.fixture.legacy;

import lombok.RequiredArgsConstructor;
import pull_up.infra.database.entity.legacy.AnswerL;
import pull_up.infra.database.entity.legacy.ProblemL;

import java.util.List;

import static pull_up.infra.database.fixture.legacy.ProblemFixture.*;

@RequiredArgsConstructor
public enum AnswerFixture {
    NO_CHOSEN_1(ONE.get(), 1L, "", false, false),
    NO_CHOSEN_2(TWO.get(), 2L,"", false, false),
    NO_CHOSEN_3(THREE.get(), 3L,"", false, false),
    NO_CHOSEN_4(FOUR.get(), 4L,"", false, false),
    NO_CHOSEN_5(FIVE.get(),5L, "", false, false),
    SOLVED_1(ONE.get(),1L, "3", true, true),
    SOLVED_2(TWO.get(),2L, "3", false, true),
    SOLVED_3(THREE.get(),3L, "3", false, true),
    SOLVED_4(FOUR.get(),4L, "3", false, true),
    SOLVED_5(FIVE.get(),5L, "3", true, true)
    ;

    private final ProblemL problemL;
    private final Long problemNumber;
    private final String chosenAnswer;
    private final Boolean isCorrect;
    private final Boolean isSolved;

    public AnswerL get() {
        AnswerL answerL = AnswerL.of(null, problemL, problemNumber, chosenAnswer, isCorrect);
        problemL.setAnswerLS(List.of(answerL));
        answerL.setIsSolved(isSolved);
        return answerL;
    }
}
