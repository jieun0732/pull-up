package pull_up.infra.database.fixture;

import lombok.RequiredArgsConstructor;
import pull_up.infra.database.entity.AnsweredProblem;
import pull_up.infra.database.entity.Problem;

import java.util.List;

import static pull_up.infra.database.fixture.ProblemFixture.*;

@RequiredArgsConstructor
public enum AnsweredProblemFixture {
    NO_CHOSEN_1(ONE.get(), "0", false),
    NO_CHOSEN_2(TWO.get(), "0", false),
    NO_CHOSEN_3(THREE.get(), "0", false),
    NO_CHOSEN_4(FOUR.get(), "0", false),
    NO_CHOSEN_5(FIVE.get(), "0", false);

    private final Problem problem;
    private final String chosenAnswer;
    private final Boolean isCorrect;

    public AnsweredProblem get() {
        AnsweredProblem answeredProblem = AnsweredProblem.of(null, problem, problem.getId(), chosenAnswer, isCorrect);
        problem.setAnsweredProblems(List.of(answeredProblem));
        return answeredProblem;
    }
}
