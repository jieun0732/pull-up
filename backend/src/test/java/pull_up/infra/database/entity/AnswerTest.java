package pull_up.infra.database.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pull_up.infra.database.fixture.AnsweredProblemFixture;

import static org.assertj.core.api.Assertions.assertThat;

class AnswerTest {

    @Test
    @DisplayName("채점 잘하는지 테스트")
    void testGrade() {
        // given
        Answer problemAnswer3 = AnsweredProblemFixture.NO_CHOSEN_1.get();
        Answer problemAnswer4 = AnsweredProblemFixture.NO_CHOSEN_1.get();

        // when
        problemAnswer3.grade(3);
        problemAnswer4.grade(4);

        // then
        assertThat(problemAnswer3.getChosenAnswer()).isEqualTo("3");
        assertThat(problemAnswer4.getChosenAnswer()).isEqualTo("4");
        assertThat(problemAnswer3.getIsCorrect()).isTrue();
        assertThat(problemAnswer4.getIsCorrect()).isFalse();
    }

}