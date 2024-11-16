package pull_up.infra.database.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pull_up.infra.database.fixture.ProblemFixture;

import static org.assertj.core.api.Assertions.assertThat;

class ProblemTest {

    @Test
    @DisplayName("공백 제거 테스트")
    void testNormalProblemType() {
        Problem problem = ProblemFixture.MATH_DENSITY_2.get();
        assertThat(problem.getNormalProblemType()).isEqualTo("용액의농도");
    }
}