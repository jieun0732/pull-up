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
    @DisplayName("사용자 푼 문제 조회 테스트 - 풀기 전")
    void testGetSolvedInfoBeforeSolve() {
        // given
        Long memberId = member.getId();
        Entry entry = Entry.MATH;
        Map<String, Integer> problemTypesMap = new HashMap<>();
        problemTypesMap.put("용액의 농도", 2);

        // when
        when(mockExamRepository.findAllEvenlyAndProblemTypeExamMap(memberId, entry)).thenReturn(new HashMap<>());
        when(mockProblemRepository.findAllProblemTypeAndCountByEntry(entry)).thenReturn(problemTypesMap);
        Solved.ByEntry.Response solvedInfo = suit.getSolvedInfo(memberId, entry);

        // then
        assertThat(solvedInfo.entry()).isEqualTo(entry);
        assertThat(solvedInfo.evenlyExamInfo().isStarted()).isFalse();
        assertThat(solvedInfo.evenlyExamInfo().isFinished()).isFalse();
        assertThat(solvedInfo.problemTypeCount()).isEqualTo(1);
        assertThat(solvedInfo.problemTypeExamInfos())
                .anySatisfy(problemTypeInfo -> {
                    assertThat(problemTypeInfo.examId()).isNull();
                    assertThat(problemTypeInfo.problemType()).isEqualTo("용액의 농도");
                    assertThat(problemTypeInfo.solvedProblemCount()).isEqualTo(0);
                    assertThat(problemTypeInfo.totalProblemCount()).isEqualTo(2);
                });
    }

    @Test
    @DisplayName("사용자 푼 문제 조회 테스트 - 골고루 문제 푼 이후")
    void testGetSolvedInfoAfterSolveEvenlyExam() {
        // given
        Long memberId = member.getId();
        Entry entry = Entry.MATH;
        Map<String, Integer> problemTypesMap = new HashMap<>();
        problemTypesMap.put("용액의 농도", 2);

        Map<String, Exam> examMap = new HashMap<>();
        Exam evenlyExam = FixtureRepository.getEvenlyExam(memberId, entry);
        evenlyExam.submit(1, 2);
        evenlyExam.submit(2, 2);
        examMap.put(ExamType.EVENLY.name(), evenlyExam);

        // when
        when(mockExamRepository.findAllEvenlyAndProblemTypeExamMap(memberId, entry)).thenReturn(examMap);
        when(mockProblemRepository.findAllProblemTypeAndCountByEntry(entry)).thenReturn(problemTypesMap);
        Solved.ByEntry.Response solvedInfo = suit.getSolvedInfo(memberId, entry);

        // then
        assertThat(solvedInfo.entry()).isEqualTo(entry);
        assertThat(solvedInfo.evenlyExamInfo().isStarted()).isTrue();
        assertThat(solvedInfo.evenlyExamInfo().isFinished()).isTrue();
        assertThat(solvedInfo.problemTypeCount()).isEqualTo(1);
        assertThat(solvedInfo.problemTypeExamInfos())
                .anySatisfy(problemTypeInfo -> {
                    assertThat(problemTypeInfo.examId()).isNull();
                    assertThat(problemTypeInfo.problemType()).isEqualTo("용액의 농도");
                    assertThat(problemTypeInfo.solvedProblemCount()).isEqualTo(0);
                    assertThat(problemTypeInfo.totalProblemCount()).isEqualTo(2);
                });
    }

    @Test
    @DisplayName("사용자 푼 문제 조회 테스트 - 유형별 문제 푼 이후")
    void testGetSolvedInfoAfterSolveByProblemTypeExam() {
        // given
        Long memberId = member.getId();
        Entry entry = Entry.MATH;
        Map<String, Integer> problemTypesMap = new HashMap<>();
        problemTypesMap.put("용액의 농도", 2);

        Map<String, Exam> examMap = new HashMap<>();
        Exam evenlyExam = FixtureRepository.getEvenlyExam(memberId, entry);
        Exam problemTypeExam = FixtureRepository.getProblemTypeExam(memberId, Entry.MATH, "용액의 농도");
        problemTypeExam.setId(1L);
        problemTypeExam.submit(1, 1);
        examMap.put(ExamType.EVENLY.name(), evenlyExam);
        examMap.put("용액의 농도", problemTypeExam);

        // when
        when(mockExamRepository.findAllEvenlyAndProblemTypeExamMap(memberId, entry)).thenReturn(examMap);
        when(mockProblemRepository.findAllProblemTypeAndCountByEntry(entry)).thenReturn(problemTypesMap);
        Solved.ByEntry.Response solvedInfo = suit.getSolvedInfo(memberId, entry);

        // then
        assertThat(solvedInfo.entry()).isEqualTo(entry);
        assertThat(solvedInfo.evenlyExamInfo().isStarted()).isTrue();
        assertThat(solvedInfo.evenlyExamInfo().isFinished()).isFalse();
        assertThat(solvedInfo.problemTypeCount()).isEqualTo(1);
        assertThat(solvedInfo.problemTypeExamInfos())
                .anySatisfy(problemTypeInfo -> {
                    assertThat(problemTypeInfo.examId()).isNotNull();
                    assertThat(problemTypeInfo.isStarted()).isTrue();
                    assertThat(problemTypeInfo.problemType()).isEqualTo("용액의 농도");
                    assertThat(problemTypeInfo.solvedProblemCount()).isEqualTo(1);
                    assertThat(problemTypeInfo.totalProblemCount()).isEqualTo(2);
                });
    }

    @Test
    @DisplayName("시험 시작 테스트 - 골고루")
    void testStartEvenlyExam() {
        // given
        Problem problem1 = ProblemFixture.LANGUAGE_COMPARE_1.get();
        Start.EvenlyRequest startReq = new Start.EvenlyRequest(member.getId(), Entry.LANGUAGE);

        // when
        when(mockMemberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(mockProblemRepository.findAllByEntry(Entry.LANGUAGE)).thenReturn(FixtureRepository.getProblemList(Entry.LANGUAGE));
        Start.Response startRes = suit.start(startReq);

        // then
        assertThat(startRes.totalProblemCount()).isEqualTo(2);
        assertThat(startRes.leftProblemCount()).isEqualTo(1);
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
        Submit.Response submitRes = suit.submit(submitReq);

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

        // when
        when(mockExamRepository.findById(any())).thenReturn(Optional.of(exam));
        Next.Response nextRes = suit.next(exam.getId(), 2);

        // then
        assertThat(nextRes.totalProblemCount()).isEqualTo(2);
        assertThat(nextRes.leftProblemCount()).isEqualTo(0);
        assertThat(nextRes.problemNumber()).isEqualTo(2);
        assertThat(nextRes.entry()).isEqualTo(problem2.getEntry());
        assertThat(nextRes.problemType()).isEqualTo(problem2.getProblemType());
        assertThat(nextRes.question()).isEqualTo(problem2.getQuestionAsString());
        assertThat(nextRes.example()).isEqualTo(problem2.getExampleAsString());
        assertThat(nextRes.choices()).contains(problem2.getChoice1(), problem2.getChoice2(), problem2.getChoice3(), problem2.getChoice4(), problem2.getChoice5());
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
    @DisplayName("시험 종료 테스트")
    void testEndExam() {
        // given
        Exam exam = FixtureRepository.getEvenlyExam(member.getId(), Entry.LANGUAGE);

        // when
        when(mockExamRepository.findById(any())).thenReturn(Optional.of(exam));
        End.Response endRes = suit.end(exam.getId());

        // then
        assertThat(endRes.entry()).isEqualTo(Entry.LANGUAGE);
        assertThat(endRes.isFinished()).isTrue();
        assertThat(endRes.memberName()).isEqualTo(member.getName());
        assertThat(endRes.totalProblemCount()).isEqualTo(2);
        assertThat(endRes.leftProblemCount()).isEqualTo(2);
        assertThat(endRes.correctProblemCount()).isEqualTo(0);
        assertThat(endRes.score()).isEqualTo(0);
        assertThat(endRes.problemResults()).hasSize(2);
        assertThat(endRes.problemResults()).allSatisfy(problemResult -> assertThat(problemResult.isSubmitted()).isFalse());

        // when 2
        suit.submit(new Submit.Request(exam.getId(), 1, 2));
        End.Response endRes2 = suit.end(exam.getId());

        // then2
        assertThat(endRes2.isFinished()).isTrue();
        assertThat(endRes2.totalProblemCount()).isEqualTo(2);
        assertThat(endRes2.leftProblemCount()).isEqualTo(1);
        assertThat(endRes2.correctProblemCount()).isEqualTo(1);
        assertThat(endRes2.score()).isEqualTo(50);
        assertThat(endRes2.problemResults()).anySatisfy(problemResult -> assertThat(problemResult.isSubmitted()).isTrue());

        // when 3
        suit.submit(new Submit.Request(exam.getId(), 2, 2));
        End.Response endRes3 = suit.end(exam.getId());

        // then2
        assertThat(endRes3.isFinished()).isTrue();
        assertThat(endRes3.totalProblemCount()).isEqualTo(2);
        assertThat(endRes3.leftProblemCount()).isEqualTo(0);
        assertThat(endRes3.correctProblemCount()).isEqualTo(1);
        assertThat(endRes3.score()).isEqualTo(50);
        assertThat(endRes3.problemResults()).allSatisfy(problemResult -> assertThat(problemResult.isSubmitted()).isTrue());

    }
}