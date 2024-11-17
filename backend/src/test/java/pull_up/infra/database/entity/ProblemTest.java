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

    @Test
    @DisplayName("정답률 계산 테스트")
    void testCalculateCorrectRate() {
        Problem problem = ProblemFixture.MATH_DENSITY_2.get();

        // add correct answer
        problem.addTotalAttempt(true);
        assertThat(problem.getCorrectRate()).isEqualTo(100);
        assertThat(problem.getIncorrectRate()).isEqualTo(0);

        // add incorrect answer
        problem.addTotalAttempt(false);
        assertThat(problem.getCorrectRate()).isEqualTo(50);
        assertThat(problem.getIncorrectRate()).isEqualTo(50);
    }
    
    @Test
    @DisplayName("정답 변환")
    void testProblemToString() {
        // given
        Problem problem = ProblemFixture.MATH_DENSITY_2.get();
    
        // when
        Integer correctAnswer = problem.getCorrectAnswerToInt();

        // then
        assertThat(correctAnswer).isEqualTo(1);
    }
}