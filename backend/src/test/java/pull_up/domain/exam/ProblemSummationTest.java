package pull_up.domain.exam;

import org.aspectj.apache.bcel.generic.TargetLostException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.Answer;
import pull_up.infra.database.jpa.fixture.FixtureRepository;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    @DisplayName("취약파트 계산 테스트")
    void testGetVulnerableEntry() {
        // given
        List<Answer> mathProblems = List.of(
                FixtureRepository.getEvenlyAnswer(1L, 1L, Entry.MATH, 1),
                FixtureRepository.getEvenlyAnswer(1L, 1L, Entry.MATH, 2),
                FixtureRepository.getEvenlyAnswer(1L, 1L, Entry.MATH, 3),
                FixtureRepository.getEvenlyAnswer(1L, 1L, Entry.MATH, 4),
                FixtureRepository.getEvenlyAnswer(1L, 1L, Entry.MATH, 5),
                FixtureRepository.getEvenlyAnswer(1L, 1L, Entry.MATH, 6),
                FixtureRepository.getEvenlyAnswer(1L, 1L, Entry.MATH, 7));
        mathProblems.subList(0, 2).forEach(answer -> answer.mark(1));
        mathProblems.subList(3, 6).forEach(answer -> answer.mark(3));
        List<Answer> languageProblems = List.of(
                FixtureRepository.getEvenlyAnswer(1L, 10L, Entry.LANGUAGE, 8),
                FixtureRepository.getEvenlyAnswer(1L, 10L, Entry.LANGUAGE, 9),
                FixtureRepository.getEvenlyAnswer(1L, 10L, Entry.LANGUAGE, 10),
                FixtureRepository.getEvenlyAnswer(1L, 10L, Entry.LANGUAGE, 11),
                FixtureRepository.getEvenlyAnswer(1L, 10L, Entry.LANGUAGE, 12),
                FixtureRepository.getEvenlyAnswer(1L, 10L, Entry.LANGUAGE, 13),
                FixtureRepository.getEvenlyAnswer(1L, 10L, Entry.LANGUAGE, 14),
                FixtureRepository.getEvenlyAnswer(1L, 10L, Entry.LANGUAGE, 15));
        languageProblems.subList(0, 2).forEach(answer -> answer.mark(1));
        languageProblems.subList(3, 7).forEach(answer -> answer.mark(4));
        List<Answer> answers = Stream.concat(mathProblems.stream(), languageProblems.stream()).toList();

        // when
        ProblemSummation problemSummation = new ProblemSummation(answers);
        List<Entry> vulnerableEntry = problemSummation.getVulnerableEntry();

        // then

        // 수리 : 3/7
        assertThat(mathProblems).hasSize(7)
                .anySatisfy(p -> {
                    assertThat(p.getIsCorrect()).isFalse();
                    assertThat(p.getProblemNumber()).isLessThan(4);
                }).anySatisfy(p -> {
                    assertThat(p.getIsCorrect()).isTrue();
                    assertThat(p.getProblemNumber()).isGreaterThanOrEqualTo(4);
                });

        // 언어 : 3/8
        assertThat(languageProblems).hasSize(8)
                .anySatisfy(p -> {
                    assertThat(p.getIsCorrect()).isFalse();
                    assertThat(p.getProblemNumber()).isLessThan(11);
                }).anySatisfy(p -> {
                    assertThat(p.getIsCorrect()).isTrue();
                    assertThat(p.getProblemNumber()).isGreaterThanOrEqualTo(11);
                });

        assertThat(vulnerableEntry).hasSize(1).allMatch(entry -> entry.equals(Entry.MATH));

        // 수리 -> 3/8
        Answer incorrectNewMathAnswer = FixtureRepository.getEvenlyAnswer(1L, 1L, Entry.MATH, 16);
        incorrectNewMathAnswer.mark(3);
        mathProblems = Stream.concat(mathProblems.stream(), Stream.of(incorrectNewMathAnswer)).toList();
        answers = Stream.concat(mathProblems.stream(), languageProblems.stream()).toList();

        problemSummation = new ProblemSummation(answers);
        vulnerableEntry = problemSummation.getVulnerableEntry();

        assertThat(vulnerableEntry).hasSize(2).contains(Entry.MATH, Entry.LANGUAGE);
    }

}