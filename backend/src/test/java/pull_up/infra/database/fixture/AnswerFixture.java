package pull_up.infra.database.fixture;

import lombok.RequiredArgsConstructor;
import pull_up.infra.database.entity.Answer;
import pull_up.infra.database.entity.Problem;

import java.util.List;

import static pull_up.infra.database.fixture.ProblemFixture.*;

@RequiredArgsConstructor
public enum AnswerFixture {
    NO_CHOSEN_1(ONE.get(), 1L, "", false),
    NO_CHOSEN_2(TWO.get(), 2L,"", false),
    NO_CHOSEN_3(THREE.get(), 3L,"", false),
    NO_CHOSEN_4(FOUR.get(), 4L,"", false),
    NO_CHOSEN_5(FIVE.get(),5L, "", false);

    private final Problem problem;
    private final Long problemNumber;
    private final String chosenAnswer;
    private final Boolean isCorrect;

    public Answer get() {
        Answer answer = Answer.of(null, problem, problemNumber, chosenAnswer, isCorrect);
        problem.setAnswers(List.of(answer));
        return answer;
    }
}
