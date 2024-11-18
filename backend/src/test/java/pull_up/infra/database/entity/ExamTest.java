package pull_up.infra.database.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pull_up.domain.exam.ExamType;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.fixture.FixtureFactory;
import pull_up.infra.database.fixture.MemberFixture;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ExamTest {

    @Test
    @DisplayName("문제 고르기 테스트")
    void testSelectEvenlyProblems() {
        // given
        List<Problem> problemList = FixtureFactory.getProblemList(4, Entry.LANGUAGE);

        // when
        List<Problem> problems = Exam.selectEvenlyProblems(problemList);

        // then
        assertThat(problems).hasSize(2);
        assertThat(problems).allSatisfy(problem -> assertThat(problem.getEntry()).isEqualTo(Entry.LANGUAGE));
    }

    @Test
    @DisplayName("문제 골고루 생성 테스트")
    void testStartEvenlyExam() {
        // given
        List<Problem> problemList = FixtureFactory.getProblemList(4, Entry.LANGUAGE);
        Member member = MemberFixture.APPLE_USER.get();

        // when
        Exam start = Exam.start(ExamType.EVENLY, member, problemList);

        // then
        assertThat(start.getExamType()).isEqualTo(ExamType.EVENLY);
        assertThat(start.getMember()).isEqualTo(member);
        assertThat(start.getAnswers()).hasSize(2);
    }
    
    @Test
    @DisplayName("골고루 문제 답안 제출 테스트")
    void testSubmit() {
        // given
        Member member = MemberFixture.APPLE_USER.get();
        Exam exam = FixtureFactory.getExam(member.getId(), ExamType.EVENLY, Entry.LANGUAGE);
        Integer problemNumber = 1;
        Integer submitAnswer = 3;

        // when
        Answer submittedAnswer = exam.submit(problemNumber, submitAnswer);

        // then
        assertThat(exam.getAnswers()).contains(submittedAnswer);
        assertThat(exam.getScore()).isEqualTo(0);
        assertThat(submittedAnswer.getExam()).isEqualTo(exam);
        assertThat(submittedAnswer.getProblem().getCorrectAnswerToInt()).isEqualTo(2);
        assertThat(submittedAnswer.getIsCorrect()).isFalse();
        assertThat(submittedAnswer.getProblemNumber()).isEqualTo(problemNumber);
        assertThat(submittedAnswer.getSubmitAnswer()).isEqualTo(submitAnswer.toString());
        assertThat(submittedAnswer.getProblem().getIncorrectAttempts()).isEqualTo(1);
        assertThat(submittedAnswer.getProblem().getIncorrectRate()).isEqualTo(100);
        assertThat(submittedAnswer.getProblem().getCorrectRate()).isEqualTo(0);

        // when 2
        problemNumber = 2;
        submitAnswer = 3;
        exam.submit(problemNumber, submitAnswer);

        // then2
        assertThat(exam.getAnswers()).contains(submittedAnswer);
        assertThat(exam.getScore()).isEqualTo(50);
    }

    @Test
    @DisplayName("시험 종료 테스트")
    void testEndExam() throws InterruptedException {
        // given
        Exam exam = FixtureFactory.getExam(MemberFixture.APPLE_USER.get().getId(), ExamType.EVENLY, Entry.LANGUAGE);

        // when
        exam.end();

        // then
        Duration duration1 = exam.getDuration();
        assertThat(exam.getIsFinished()).isFalse();
        assertThat(exam.getScore()).isEqualTo(0);
        assertThat(duration1).isNotNull();

        // when 2
        Thread.sleep(100);
        exam.submit(1, 3);
        exam.end();

        // then 2
        Duration duration2 = exam.getDuration();
        assertThat(exam.getIsFinished()).isFalse();
        assertThat(exam.getScore()).isEqualTo(0);
        assertThat(duration2).isGreaterThan(duration1);

        // when 3
        Thread.sleep(100);
        exam.submit(2, 3);
        exam.end();

        // then 3
        assertThat(exam.getIsFinished()).isTrue();
        assertThat(exam.getScore()).isEqualTo(50);
        assertThat(exam.getDuration()).isGreaterThan(duration2);
    }
}