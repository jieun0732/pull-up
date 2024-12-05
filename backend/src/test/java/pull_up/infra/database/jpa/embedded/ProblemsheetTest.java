package pull_up.infra.database.jpa.embedded;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pull_up.infra.database.jpa.fixture.FixtureRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProblemsheetTest {

    @Test
    @DisplayName("문제 교체 테스트")
    void testChangeProblem() {
        // given
        Problemsheet problemsheet = FixtureRepository.getProblemsheetEntity(1, 1L);

        // when
        problemsheet.changeProblem(1000L);

        // then
        assertThat(problemsheet.getProblemNumber()).isEqualTo(1);
        assertThat(problemsheet.getProblemId()).isEqualTo(1000L);
    }
}