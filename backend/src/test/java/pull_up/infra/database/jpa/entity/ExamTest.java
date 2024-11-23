package pull_up.infra.database.jpa.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pull_up.domain.exam.ExamType;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.fixture.FixtureRepository;
import pull_up.infra.database.jpa.fixture.MemberFixture;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ExamTest {

    @Test
    @DisplayName("문제 고르기 테스트")
    void testSelectEvenlyProblems() {
        // given
        List<Problem> problemList = FixtureRepository.getProblemList(Entry.LANGUAGE);

        // when
        List<Problem> problems = Exam.selectEvenlyProblems(problemList);

        Duration duration = Duration.between(LocalDateTime.of(2022, 10, 15, 10, 20), LocalDateTime.of(2022, 10, 15, 10, 30));
        System.out.println("Duration = " + duration);
        String s = duration.toString();
        System.out.println("Duration.parse(s) = " + Duration.parse(s));

        // then
        assertThat(problems).hasSize(2);
        assertThat(problems).allSatisfy(problem -> assertThat(problem.getEntry()).isEqualTo(Entry.LANGUAGE));
    }

    @Test
    @DisplayName("문제 골고루 생성 테스트")
    void testStartEvenlyExam() {
        // given
        List<Problem> problemList = FixtureRepository.getProblemList(Entry.LANGUAGE);
        Member member = MemberFixture.APPLE_USER.get();

        // when
        Exam start = Exam.startEvenlyExam(member, problemList);

        // then
        assertThat(start.getExamType()).isEqualTo(ExamType.EVENLY);
        assertThat(start.getMember()).isEqualTo(member);
        assertThat(start.getAnswers()).hasSize(2);
    }

    @Test
    @DisplayName("문제 유형별 생성 테스트")
    void testStartByProblemTypeExam() {
        // given
        List<Problem> problemList = FixtureRepository.getProblemList(Entry.MATH, "용액의 농도");
        Member member = MemberFixture.APPLE_USER.get();

        // when
        Exam start = Exam.startByProblemTypeExam(member, problemList);

        // then
        assertThat(start.getExamType()).isEqualTo(ExamType.BY_PROBLEM_TYPE);
        assertThat(start.getMember()).isEqualTo(member);
        assertThat(start.getAnswers()).hasSize(2);
    }

    @Test
    @DisplayName("모의고사 생성 테스트")
    void testStartMockExam() {
        // given
        Member member = MemberFixture.APPLE_USER.get();
        List<Problem> problemList = FixtureRepository.getProblemList();
        Examsheet examsheet = FixtureRepository.getExamsheet("모의고사");

        // when
        Exam start = Exam.startMockExam(member, problemList, examsheet);

        // then
        assertThat(start.getExamType()).isEqualTo(ExamType.MOCK_EXAM);
        assertThat(start.getMember()).isEqualTo(member);
        assertThat(start.getAnswers()).hasSize(12);
    }

    @Test
    @DisplayName("문제 답안 제출 테스트")
    void testSubmit() {
        // given
        Member member = MemberFixture.APPLE_USER.get();
        Exam exam = FixtureRepository.getEvenlyExam(member.getId(), Entry.LANGUAGE);
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
    @DisplayName("모의고사 채점 테스트")
    void testGrade() {
        // given
        Member member = MemberFixture.APPLE_USER.get();
        Exam exam = FixtureRepository.getMockExam(member.getId());
        Map<Integer, Integer> answerSheet = new HashMap<>();
        answerSheet.put(1, 3);
        answerSheet.put(2, 3);
        answerSheet.put(3, 3);
        answerSheet.put(4, 3);
        answerSheet.put(5, 3);
        answerSheet.put(6, 3);
        answerSheet.put(7, 3);
        answerSheet.put(8, 3);
        answerSheet.put(9, 3);
        answerSheet.put(10, 3);
        answerSheet.put(11, 3);
        answerSheet.put(12, 3);

        // when
        exam.grade(answerSheet);

        // then
        assertThat(exam.getAnswers()).hasSize(12)
                .allSatisfy(answer -> assertThat(answer.getIsSubmitted()).isTrue());
        assertThat(exam.getScore()).isEqualTo(33);
        assertThat(exam.getDuration()).isNotNull();
        assertThat(exam.getExamsheet().getExamCount()).isEqualTo(1);
        assertThat(exam.getExamsheet().getAverageScore()).isEqualTo(33);
    }

    @Test
    @DisplayName("시험 종료 테스트")
    void testEndExam() throws InterruptedException {
        // given

        // when
        Exam exam = FixtureRepository.getEvenlyExam(MemberFixture.APPLE_USER.get().getId(), Entry.LANGUAGE);

        // then
        assertThat(exam.getIsFinished()).isFalse();
        assertThat(exam.getScore()).isEqualTo(0);

        // when 2
        Thread.sleep(100);
        exam.submit(1, 3);

        // then 2
        Duration duration2 = exam.getDuration();
        assertThat(exam.getIsFinished()).isFalse();
        assertThat(exam.getScore()).isEqualTo(0);

        // when 3
        Thread.sleep(100);
        exam.submit(2, 3);

        // then 3
        assertThat(exam.getIsFinished()).isTrue();
        assertThat(exam.getScore()).isEqualTo(50);
        assertThat(exam.getDuration()).isGreaterThan(duration2);
    }

    @Test
    @DisplayName("마지막으로 푼 문제번호 가져오기 테스트")
    void testGetLastSolvedProblem() {
        // given
        Exam exam = FixtureRepository.getEvenlyExam(MemberFixture.APPLE_USER.get().getId(), Entry.LANGUAGE);

        // when
        Integer lastSolvedProblem = exam.getLastSolvedProblem();

        // then
        assertThat(lastSolvedProblem).isEqualTo(0);

        // when2 : 1번문제 풀이 후
        exam.submit(1, 3);
        lastSolvedProblem = exam.getLastSolvedProblem();

        // then2
        assertThat(lastSolvedProblem).isEqualTo(1);

        // when3 : 2번문제 풀이 후
        exam.submit(2, 3);
        lastSolvedProblem = exam.getLastSolvedProblem();

        // then2
        assertThat(lastSolvedProblem).isEqualTo(2);
    }

    @Test
    @DisplayName("시험 리셋 테스트")
    void testReset() {
        // given
        Exam exam = FixtureRepository.getEvenlyExam(MemberFixture.APPLE_USER.get().getId(), Entry.LANGUAGE);
        exam.submit(1, 2);

        // when
        exam.reset();

        // then
        assertThat(exam.getDuration()).isNull();
        assertThat(exam.getStartTime()).isNull();
        assertThat(exam.getEndTime()).isNull();
        assertThat(exam.getScore()).isZero();
        assertThat(exam.getAnswers()).allSatisfy(answer -> {
                    assertThat(answer.getIsSubmitted()).isFalse();
                    assertThat(answer.getSubmitAnswer()).isNull();
                    assertThat(answer.getIsCorrect()).isNull();
                    assertThat(answer.getSubmitCount()).isZero();
                }
        );
    }
}