package pull_up.infra.database.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pull_up.infra.database.fixture.ProblemFixture;

class ProblemTest {

    @Test
    @DisplayName("오답률 계산 잘하는지 테스트")
    void testCalculateIncorrectRate() {
        // given
        Problem problem = ProblemFixture.ONE.get();

        problem.addTotalAttempt(true);
        Assertions.assertThat(problem.getIncorrectRate()).isEqualTo(0);

        problem.addTotalAttempt(false);
        Assertions.assertThat(problem.getIncorrectRate()).isEqualTo((double) 1 / 2 * 100);

        problem.addTotalAttempt(false);
        Assertions.assertThat(problem.getIncorrectRate()).isEqualTo((double) 2 / 3 * 100);
    }
}