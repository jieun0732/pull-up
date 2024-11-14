package pull_up.infra.database.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pull_up.infra.database.fixture.ExamFixture;
import pull_up.infra.database.fixture.MemberFixture;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ExamTest {

    @Test
    @DisplayName("제출한 문제로 채점 테스트")
     void testGrade() {
        // given
        Exam exam = ExamFixture.MATHEMATICS.get(MemberFixture.APPLE_USER.get());
        Map<Long, Integer> submit = new HashMap<>();
        submit.put(1L, 3);
        submit.put(2L, 3);
        submit.put(3L, 3);
        submit.put(4L, 3);
        submit.put(5L, 3);

        // when
        int[] grade = exam.grade(submit);

        // then
        assertThat(grade[0]).isEqualTo(2);
        assertThat(grade[1]).isEqualTo(3);
        assertThat(exam.getAnswers())
                .anySatisfy(answer -> assertAnswer(answer, 1L, true))
                .anySatisfy(answer -> assertAnswer(answer, 2L, false))
                .anySatisfy(answer -> assertAnswer(answer, 3L, false))
                .anySatisfy(answer -> assertAnswer(answer, 4L, false))
                .anySatisfy(answer -> assertAnswer(answer, 5L, true));

    }

    private static void assertAnswer(Answer answer, long problemNumber, boolean isCorrect) {
        assertThat(answer.getProblemNumber()).isEqualTo(problemNumber);
        assertThat(answer.getIsCorrect()).isEqualTo(isCorrect);
    }
}