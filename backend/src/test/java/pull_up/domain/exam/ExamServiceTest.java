package pull_up.domain.exam;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pull_up.domain.dao.ExamRepository;
import pull_up.domain.exam.dto.*;
import pull_up.domain.dao.MemberRepository;
import pull_up.domain.dao.ExamsheetRepository;
import pull_up.domain.exam.exception.ExamErrorCode;
import pull_up.domain.problem.Entry;
import pull_up.domain.dao.ProblemRepository;
import pull_up.infra.database.jpa.entity.Exam;
import pull_up.infra.database.jpa.entity.Examsheet;
import pull_up.infra.database.jpa.entity.Member;
import pull_up.infra.database.jpa.entity.Problem;
import pull_up.infra.database.jpa.fixture.FixtureRepository;
import pull_up.infra.database.jpa.fixture.MemberFixture;
import pull_up.infra.database.jpa.fixture.ProblemFixture;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExamServiceTest {

    ExamService suit;

    ExamRepository mockExamRepository;
    MemberRepository mockMemberRepository;
    ProblemRepository mockProblemRepository;
    ExamsheetRepository mockExamsheetRepository;

    Member member;

    @BeforeEach
    void init() {
        mockExamRepository = mock(ExamRepository.class);
        mockMemberRepository = mock(MemberRepository.class);
        mockProblemRepository = mock(ProblemRepository.class);
        mockExamsheetRepository = mock(ExamsheetRepository.class);
        suit = new ExamService(mockExamRepository, mockMemberRepository, mockProblemRepository, mockExamsheetRepository);

        member = MemberFixture.APPLE_USER.get();
    }

    @Test
    @DisplayName("시험 시작 테스트 - 골고루")
    void testStartEvenlyExamV2() {
        // given
        Examsheet examSheet = FixtureRepository.getExamsheet(ExamType.MOCK_EXAM.name());
        List<Problem> problemList = FixtureRepository.getProblemList();
        Start.EvenlyRequest startReq = new Start.EvenlyRequest(member.getId(), Entry.LANGUAGE);

        // when
        when(mockMemberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(mockExamsheetRepository.findByExamTitle("EVENLY_LANGUAGE")).thenReturn(examSheet);
        when(mockProblemRepository.findAllById(any())).thenReturn(problemList);
        Start.Response startRes = suit.start(startReq);

        // then
        Problem problem1 = problemList.get(0);
        assertThat(startRes.totalProblemCount()).isEqualTo(12);
        assertThat(startRes.leftProblemCount()).isEqualTo(11);
        assertThat(startRes.problemNumber()).isEqualTo(1);
        assertThat(startRes.entry()).isEqualTo(problem1.getEntry());
        assertThat(startRes.problemType()).isEqualTo(problem1.getProblemType());
        assertThat(startRes.question()).isEqualTo(problem1.getQuestionAsString());
        assertThat(startRes.example()).isEqualTo(problem1.getExampleAsString());
        assertThat(startRes.choices()).contains(problem1.getChoice1(), problem1.getChoice2(), problem1.getChoice3(), problem1.getChoice4(), problem1.getChoice5());
    }

    @Test
    @DisplayName("시험 시작 테스트 - 모의고사")
    void testStartMockExam() {
        // given
        Member member = MemberFixture.APPLE_USER.get();
        Examsheet examSheet = FixtureRepository.getExamsheet(ExamType.MOCK_EXAM.name());
        List<Problem> problemList = FixtureRepository.getProblemList();
        Start.MockExamRequest startReq = new Start.MockExamRequest(member.getId(), ExamType.MOCK_EXAM.name());

        // when
        when(mockMemberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(mockExamsheetRepository.findByExamTitle(ExamType.MOCK_EXAM.name())).thenReturn(examSheet);
        when(mockProblemRepository.findAllById(any())).thenReturn(problemList);
        Start.Response startRes = suit.start(startReq);

        // then
        Problem problem1 = problemList.get(0);
        assertThat(startRes.totalProblemCount()).isEqualTo(12);
        assertThat(startRes.leftProblemCount()).isEqualTo(11);
        assertThat(startRes.problemNumber()).isEqualTo(1);
        assertThat(startRes.entry()).isEqualTo(problem1.getEntry());
        assertThat(startRes.problemType()).isEqualTo(problem1.getProblemType());
        assertThat(startRes.question()).isEqualTo(problem1.getQuestionAsString());
        assertThat(startRes.example()).isEqualTo(problem1.getExampleAsString());
        assertThat(startRes.choices()).contains(problem1.getChoice1(), problem1.getChoice2(), problem1.getChoice3(), problem1.getChoice4(), problem1.getChoice5());
    }

    @Test
    @DisplayName("시험 제출 테스트")
    void testSubmitAnswer() {
        // given
        Integer problemNumber = 1;
        Integer submitAnswer = 1;
        Exam exam = FixtureRepository.getEvenlyExam(member.getId(), Entry.LANGUAGE);
        Problem problem1 = exam.getAnswers().get(0).getProblem();

        Submit.Request submitReq = new Submit.Request(exam.getId(), problemNumber, submitAnswer);

        // when
        when(mockExamRepository.findById(any())).thenReturn(Optional.of(exam));
        Explanation submitRes = suit.submit(submitReq);

        // then
        assertThat(submitRes).isNotNull();
        assertThat(submitRes.isCorrect()).isFalse();
        assertThat(submitRes.correctAnswer()).isEqualTo(problem1.getCorrectAnswerToInt());
        assertThat(submitRes.explanation()).isEqualTo(problem1.getExplanationAsString());
        assertThat(submitRes.incorrectRate()).isEqualTo(100D);
        assertThat(submitRes.correctRate()).isEqualTo(0D);
    }

    @Test
    @DisplayName("다음 문제 요청 테스트")
    void testNextProblem() {
        // given
        Exam exam = FixtureRepository.getEvenlyExam(member.getId(), Entry.LANGUAGE);
        Problem problem2 = ProblemFixture.LANGUAGE_REASONING_1.get();

        // when : 안푼 문제 조회 시
        when(mockExamRepository.findById(any())).thenReturn(Optional.of(exam));
        Next.Response nextRes = suit.next(exam.getId(), 2);

        // then
        assertThat(nextRes.isSubmitted()).isFalse();
        assertThat(nextRes.explanation()).isNull();
        assertThat(nextRes.totalProblemCount()).isEqualTo(2);
        assertThat(nextRes.leftProblemCount()).isEqualTo(0);
        assertThat(nextRes.problemNumber()).isEqualTo(2);
        assertThat(nextRes.entry()).isEqualTo(problem2.getEntry());
        assertThat(nextRes.problemType()).isEqualTo(problem2.getProblemType());
        assertThat(nextRes.question()).isEqualTo(problem2.getQuestionAsString());
        assertThat(nextRes.example()).isEqualTo(problem2.getExampleAsString());
        assertThat(nextRes.choices()).contains(problem2.getChoice1(), problem2.getChoice2(), problem2.getChoice3(), problem2.getChoice4(), problem2.getChoice5());

        // when2 : 푼 문제 조회 시
        exam.submit(1, 1);
        Next.Response nextRes2 = suit.next(exam.getId(), 1);

        // then2
        assertThat(nextRes2.isSubmitted()).isTrue();
        assertThat(nextRes2.explanation()).isNotNull();
    }

    @Test
    @DisplayName("시험 이어하기 테스트")
    void testContinue() {
        // given
        Exam exam = FixtureRepository.getEvenlyExam(member.getId(), Entry.LANGUAGE);
        exam.submit(1,3);

        // when
        when(mockExamRepository.findById(any())).thenReturn(Optional.of(exam));
        Next.Response continueRes = suit.continueExam(exam.getId());

        // then
        assertThat(continueRes.leftProblemCount()).isEqualTo(0);
        assertThat(continueRes.totalProblemCount()).isEqualTo(2);
        assertThat(continueRes.problemNumber()).isEqualTo(2);

        // when2 : 문제 다 풀었을 경우
        exam.submit(2, 3);

        // then2 : 오류 발생
        assertThatThrownBy(() -> suit.continueExam(exam.getId()))
                .hasMessage(ExamErrorCode.PROBLEM_NUMBER_EXCEED.getMessage());
    }

    @Test
    @DisplayName("골고루/유형별 시험 종료 테스트")
    void testGetEntryExamResult() {
        // given
        Exam exam = FixtureRepository.getEvenlyExam(member.getId(), Entry.LANGUAGE);

        // when
        when(mockExamRepository.findById(any())).thenReturn(Optional.of(exam));
        Result.ByEntryResponse endRes = suit.getEntryExamResult(exam.getId());

        // then
        assertThat(endRes.entry()).isEqualTo(Entry.LANGUAGE);
        assertThat(endRes.isFinished()).isFalse();
        assertThat(endRes.memberName()).isEqualTo(member.getName());
        assertThat(endRes.totalProblemCount()).isEqualTo(2);
        assertThat(endRes.leftProblemCount()).isEqualTo(2);
        assertThat(endRes.correctProblemCount()).isEqualTo(0);
        assertThat(endRes.score()).isEqualTo(0);
        assertThat(endRes.results()).hasSize(2);
        assertThat(endRes.results()).allSatisfy(problemResult -> assertThat(problemResult.isSubmitted()).isFalse());

        // when 2
        suit.submit(new Submit.Request(exam.getId(), 1, 2));
        Result.ByEntryResponse endRes2 = suit.getEntryExamResult(exam.getId());

        // then2
        assertThat(endRes2.isFinished()).isFalse();
        assertThat(endRes2.totalProblemCount()).isEqualTo(2);
        assertThat(endRes2.leftProblemCount()).isEqualTo(1);
        assertThat(endRes2.correctProblemCount()).isEqualTo(1);
        assertThat(endRes2.score()).isEqualTo(50);
        assertThat(endRes2.results()).anySatisfy(problemResult -> assertThat(problemResult.isSubmitted()).isTrue());

        // when 3
        suit.submit(new Submit.Request(exam.getId(), 2, 2));
        Result.ByEntryResponse endRes3 = suit.getEntryExamResult(exam.getId());

        // then2
        assertThat(endRes3.isFinished()).isTrue();
        assertThat(endRes3.totalProblemCount()).isEqualTo(2);
        assertThat(endRes3.leftProblemCount()).isEqualTo(0);
        assertThat(endRes3.correctProblemCount()).isEqualTo(1);
        assertThat(endRes3.score()).isEqualTo(50);
        assertThat(endRes3.results()).allSatisfy(problemResult -> assertThat(problemResult.isSubmitted()).isTrue());
    }

    @Test
    @DisplayName("모의고사 채점 테스트")
    void testGradeExam() {
        // given
        Exam exam = FixtureRepository.getMockExam(member.getId());
        Grade.Request gradeReq = new Grade.Request(exam.getId(), List.of(
                new Grade.Request.AnswerSheet(1, 3),
                new Grade.Request.AnswerSheet(2, 3),
                new Grade.Request.AnswerSheet(3, 3),
                new Grade.Request.AnswerSheet(4, 3),
                new Grade.Request.AnswerSheet(5, 3),
                new Grade.Request.AnswerSheet(6, 3),
                new Grade.Request.AnswerSheet(7, 3),
                new Grade.Request.AnswerSheet(8, 3),
                new Grade.Request.AnswerSheet(9, 3),
                new Grade.Request.AnswerSheet(10, 3),
                new Grade.Request.AnswerSheet(11, 3),
                new Grade.Request.AnswerSheet(12, 3)));

        // when
        when(mockExamRepository.findById(any())).thenReturn(Optional.of(exam));
        Report.mockExam report = suit.grade(gradeReq);

        // then
        assertThat(report.examId()).isEqualTo(exam.getId());
        assertThat(report.name()).isEqualTo(member.getName());
        assertThat(report.scoreInfo().myScore()).isEqualTo(33);
        assertThat(report.scoreInfo().averageScore()).isEqualTo(33);
        assertThat(report.scoreInfo().topRate()).isEqualTo(33);
        assertThat(report.durationInfo().timeLimit()).isEqualTo(20);
        assertThat(report.durationInfo().myDurationMinute()).isLessThan(1);
        assertThat(report.durationInfo().averageDurationMinute()).isLessThan(1);
        assertThat(report.vulnerableEntryInfo().vulnerableEntry()).contains(Entry.MATH.getKorean(), Entry.REASONING.getKorean());
        assertThat(report.vulnerableEntryInfo().totalLanguageCount()).isEqualTo(4);
        assertThat(report.vulnerableEntryInfo().totalMathCount()).isEqualTo(4);
        assertThat(report.vulnerableEntryInfo().totalReasoningCount()).isEqualTo(4);
        assertThat(report.vulnerableEntryInfo().incorrectLanguageCount()).isEqualTo(2);
        assertThat(report.vulnerableEntryInfo().incorrectMathCount()).isEqualTo(3);
        assertThat(report.vulnerableEntryInfo().incorrectReasoningCount()).isEqualTo(3);
    }

    @Test
    @DisplayName("모의고사 시험 종료 테스트")
    void testGetMockExamResult() {
        // given
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

        // when(채점 전)
        when(mockExamRepository.findById(any())).thenReturn(Optional.of(exam));

        // then(오류)
        assertThatThrownBy(() -> suit.getMockExamResult(exam.getId())).hasMessage(ExamErrorCode.NOT_GRADED_MOCK_EXAM.getMessage());

        // when 2(채점 후)
        exam.grade(answerSheet);
        Result.MockExamResponse endRes = suit.getMockExamResult(exam.getId());

        // then 2(결과 반환)
        assertThat(endRes.memberName()).isEqualTo(member.getName());
        assertThat(endRes.totalProblemCount()).isEqualTo(12);
        assertThat(endRes.correctProblemCount()).isEqualTo(4);
        assertThat(endRes.score()).isEqualTo(33);
        assertThat(endRes.durationSecond()).isLessThan(1L);
        assertThat(endRes.results()).allSatisfy(problemResult -> assertThat(problemResult.isSubmitted()).isTrue());
    }
}