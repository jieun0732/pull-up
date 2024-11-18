package pull_up.infra.database.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pull_up.domain.exam.ExamType;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.fixture.FixtureFactory;

import static org.assertj.core.api.Assertions.assertThat;

class AnswerTest {

    @Test
    @DisplayName("채점 테스트")
    void testMark() {
        // given
        Integer submitAnswer = 3;
        Answer emptyAnswer = FixtureFactory.getEmptyAnswer(1, 1L, ExamType.EVENLY, Entry.MATH);

        // when
        boolean correct = emptyAnswer.mark(submitAnswer);

        // then
        assertThat(correct).isTrue();
        assertThat(emptyAnswer.getSubmitAnswerToInt()).isEqualTo(submitAnswer);
        assertThat(emptyAnswer.getIsCorrect()).isTrue();
        assertThat(emptyAnswer.getSubmitCount()).isEqualTo(1);
    }
}