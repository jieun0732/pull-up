package pull_up.domain.exam;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.Answer;
import pull_up.infra.database.jpa.fixture.AnswerFixture;
import pull_up.infra.database.jpa.fixture.FixtureRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ProblemSummationTest {

    @Test
    @DisplayName("Summation 생성 테스트")
    void testCreateSummation() {
        // given
        Answer solvedProblem1 = FixtureRepository.getEvenlyAnswer(1L, 1L, Entry.MATH, 1);
        Answer solvedProblem2 = FixtureRepository.getEvenlyAnswer(1L, 5L, Entry.REASONING, 2);
        Answer notSolvedProblem = FixtureRepository.getEvenlyAnswer(1L, 10L, Entry.LANGUAGE, 3);
        solvedProblem1.mark(2);
        solvedProblem2.mark(1);
        List<Answer> answers = List.of(solvedProblem1, solvedProblem2, notSolvedProblem);

        // when
        ProblemSummation problemSummation = new ProblemSummation(answers);

        // then
        assertThat(problemSummation.getTotalProblemCount()).isEqualTo(3);
        assertThat(problemSummation.getSolvedProblemCount()).isEqualTo(2);
        assertThat(problemSummation.getLeftProblemCount()).isEqualTo(1);
        assertThat(problemSummation.getCorrectProblemCount()).isEqualTo(1);
        assertThat(problemSummation.getIncorrectProblemCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("EntrySummation 생성 테스트")
    void testCreateEntrySummation() {
        // given
        Answer solvedProblem1 = FixtureRepository.getEvenlyAnswer(1L, 1L, Entry.MATH, 1);
        Answer solvedProblem2 = FixtureRepository.getEvenlyAnswer(1L, 5L, Entry.REASONING, 2);
        Answer notSolvedProblem = FixtureRepository.getEvenlyAnswer(1L, 10L, Entry.LANGUAGE, 3);
        solvedProblem1.mark(2);
        solvedProblem2.mark(1);
        List<Answer> answers = List.of(solvedProblem1, solvedProblem2, notSolvedProblem);

        // when
        ProblemSummation problemSummation = new ProblemSummation(answers);
        List<Entry> vulnerableEntry = problemSummation.getVulnerableEntry();

        // then
        assertThat(problemSummation.getEntrySummations().get(Entry.MATH).getTotalCount()).isEqualTo(1);
        assertThat(problemSummation.getEntrySummations().get(Entry.MATH).getIncorrectCount()).isEqualTo(1);
        assertThat(problemSummation.getEntrySummations().get(Entry.MATH).getCorrectCount()).isEqualTo(0);
        assertThat(problemSummation.getEntrySummations().get(Entry.REASONING).getTotalCount()).isEqualTo(1);
        assertThat(problemSummation.getEntrySummations().get(Entry.REASONING).getIncorrectCount()).isEqualTo(0);
        assertThat(problemSummation.getEntrySummations().get(Entry.REASONING).getCorrectCount()).isEqualTo(1);
        assertThat(problemSummation.getEntrySummations().get(Entry.LANGUAGE).getTotalCount()).isEqualTo(0);
        assertThat(problemSummation.getEntrySummations().get(Entry.LANGUAGE).getIncorrectCount()).isEqualTo(0);
        assertThat(problemSummation.getEntrySummations().get(Entry.LANGUAGE).getCorrectCount()).isEqualTo(0);
        assertThat(vulnerableEntry).hasSize(1).allMatch(entry -> entry.equals(Entry.MATH));
    }

}