package pull_up.infra.database.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pull_up.infra.database.entity.legacy.AnswerL;
import pull_up.infra.database.fixture.legacy.AnswerFixture;

import static org.assertj.core.api.Assertions.assertThat;

class AnswerLTest {

    @Test
    @DisplayName("채점 잘하는지 테스트")
    void testMark() {
        // given
        AnswerL problemAnswerL = AnswerFixture.NO_CHOSEN_1.get();

        // when
        problemAnswerL.mark(3);

        // then
        assertThat(problemAnswerL.getChosenAnswer()).isEqualTo("3");
        assertThat(problemAnswerL.getIsCorrect()).isTrue();
        assertThat(problemAnswerL.getTryCount()).isEqualTo(1);
        assertThat(problemAnswerL.getIsSolved()).isTrue();

        // when 2
        problemAnswerL.mark(4);

        // then 2
        assertThat(problemAnswerL.getChosenAnswer()).isEqualTo("4");
        assertThat(problemAnswerL.getIsCorrect()).isFalse();
        assertThat(problemAnswerL.getTryCount()).isEqualTo(2);
    }

}