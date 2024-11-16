package pull_up.infra.database.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pull_up.domain.exam.ExamType;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.fixture.FixtureFactory;
import pull_up.infra.database.fixture.MemberFixture;

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
}