package pull_up.domain.exam;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pull_up.infra.database.jpa.entity.Answer;
import pull_up.infra.database.jpa.fixture.AnswerFixture;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProblemSummationTest {

    @Test
    @DisplayName("Summation 생성 테스트")
    void testCreateSummation() {
        // given
        List<Answer> answers = List.of(AnswerFixture.CORRECT_6.get(), AnswerFixture.NOT_SUBMITTED_11.get(), AnswerFixture.INCORRECT_1.get());

        // when
        ProblemSummation problemSummation = ProblemSummation.createProblemSummation(answers);

        // then
        assertThat(problemSummation.getTotalProblemCount()).isEqualTo(3);
        assertThat(problemSummation.getSolvedProblemCount()).isEqualTo(2);
        assertThat(problemSummation.getLeftProblemCount()).isEqualTo(1);
        assertThat(problemSummation.getCorrectProblemCount()).isEqualTo(1);
        assertThat(problemSummation.getIncorrectProblemCount()).isEqualTo(1);
    }

}