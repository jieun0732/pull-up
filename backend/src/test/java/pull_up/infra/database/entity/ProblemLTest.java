package pull_up.infra.database.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pull_up.infra.database.entity.legacy.ProblemL;
import pull_up.infra.database.fixture.legacy.ProblemFixture;

class ProblemLTest {

    @Test
    @DisplayName("오답률 계산 잘하는지 테스트")
    void testCalculateIncorrectRate() {
        // given
        ProblemL problemL = ProblemFixture.ONE.get();

        problemL.addTotalAttempt(true);
        Assertions.assertThat(problemL.getIncorrectRate()).isEqualTo(0);

        problemL.addTotalAttempt(false);
        Assertions.assertThat(problemL.getIncorrectRate()).isEqualTo((double) 1 / 2 * 100);

        problemL.addTotalAttempt(false);
        Assertions.assertThat(problemL.getIncorrectRate()).isEqualTo((double) 2 / 3 * 100);
    }
}