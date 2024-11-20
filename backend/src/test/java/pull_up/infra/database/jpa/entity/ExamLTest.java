package pull_up.infra.database.jpa.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pull_up.infra.database.jpa.entity.legacy.AnswerL;
import pull_up.infra.database.jpa.entity.legacy.ExamL;
import pull_up.infra.database.jpa.fixture.legacy.ExamFixture;
import pull_up.infra.database.jpa.fixture.legacy.MemberFixture;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ExamLTest {

    @Test
    @DisplayName("제출한 문제로 채점 테스트")
     void testGrade() {
        // given
        ExamL examL = ExamFixture.MATHEMATICS.get(MemberFixture.APPLE_USER.get());
        Map<Long, Integer> submit = new HashMap<>();
        submit.put(1L, 3);
        submit.put(2L, 3);
        submit.put(3L, 3);
        submit.put(4L, 3);
        submit.put(5L, 3);

        // when
        int[] grade = examL.grade(submit);

        // then
        assertThat(grade[0]).isEqualTo(2);
        assertThat(grade[1]).isEqualTo(3);
        assertThat(examL.getAnswerLS())
                .anySatisfy(answer -> assertAnswer(answer, 1L, true))
                .anySatisfy(answer -> assertAnswer(answer, 2L, false))
                .anySatisfy(answer -> assertAnswer(answer, 3L, false))
                .anySatisfy(answer -> assertAnswer(answer, 4L, false))
                .anySatisfy(answer -> assertAnswer(answer, 5L, true));

    }

    @Test
    @DisplayName("문제 번호로 제출한 문제 가져오기")
    void testGetAnswer() {
        // given
        ExamL examL = ExamFixture.MATHEMATICS.get(MemberFixture.APPLE_USER.get());

        // when
        AnswerL answerL = examL.getAnswer(1L);

        // then
        assertThat(answerL.getProblemNumber()).isEqualTo(1L);
        assertThat(answerL.getProblemL().getQuestion()).isEqualTo("철수가 출발 지점에서 10km 떨어진 지점에서부터 시계 방향으로 원형 트랙을 자전거로 돌기 시작했다. 이 원형 트랙의 총 길이는 8km이다. 철수는 처음 2시간 동안 5바퀴를 돌았고, 다음 1시간 동안 3바퀴를 돌았다면, 철수의 3시간 동안의 자전거 평균 속력은 몇 km/h인가? (소수점 둘째자리에서 반올림하세요.)");
    }

    private static void assertAnswer(AnswerL answerL, long problemNumber, boolean isCorrect) {
        assertThat(answerL.getProblemNumber()).isEqualTo(problemNumber);
        assertThat(answerL.getIsCorrect()).isEqualTo(isCorrect);
    }
}