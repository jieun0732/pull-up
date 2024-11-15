package pull_up.infra.database.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pull_up.infra.database.fixture.AnswerFixture;

import static org.assertj.core.api.Assertions.assertThat;

class AnswerTest {

    @Test
    @DisplayName("채점 잘하는지 테스트")
    void testMark() {
        // given
        Answer problemAnswer = AnswerFixture.NO_CHOSEN_1.get();

        // when
        problemAnswer.mark(3);

        // then
        assertThat(problemAnswer.getChosenAnswer()).isEqualTo("3");
        assertThat(problemAnswer.getIsCorrect()).isTrue();
        assertThat(problemAnswer.getTryCount()).isEqualTo(1);

        // when 2
        problemAnswer.mark(4);

        // then 2
        assertThat(problemAnswer.getChosenAnswer()).isEqualTo("4");
        assertThat(problemAnswer.getIsCorrect()).isFalse();
        assertThat(problemAnswer.getTryCount()).isEqualTo(2);
    }

}