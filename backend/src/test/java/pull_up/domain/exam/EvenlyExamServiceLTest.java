package pull_up.domain.exam;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pull_up.domain.dao.ExamRepository;
import pull_up.domain.exam.dto.End;
import pull_up.domain.exam.dto.Next;
import pull_up.domain.exam.dto.Start;
import pull_up.domain.exam.dto.Submit;
import pull_up.domain.dao.MemberRepository;
import pull_up.domain.problem.Entry;
import pull_up.domain.dao.ProblemRepository;
import pull_up.infra.database.entity.Exam;
import pull_up.infra.database.entity.Member;
import pull_up.infra.database.entity.Problem;
import pull_up.infra.database.fixture.FixtureFactory;
import pull_up.infra.database.fixture.MemberFixture;
import pull_up.infra.database.fixture.ProblemFixture;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class EvenlyExamServiceLTest {

    EvenlyExamService suit;

    ExamRepository mockExamRepository;
    MemberRepository mockMemberRepository;
    ProblemRepository mockProblemRepository;

    Member member;

    @BeforeEach
    void init() {
        mockExamRepository = mock(ExamRepository.class);
        mockMemberRepository = mock(MemberRepository.class);
        mockProblemRepository = mock(ProblemRepository.class);
        suit = new EvenlyExamService(mockExamRepository, null, mockMemberRepository, mockProblemRepository);

        member = MemberFixture.APPLE_USER.get();
    }

    @Test
    @DisplayName("시험 시작 테스트")
    void testStartExam() {
        // given
        Problem problem1 = ProblemFixture.LANGUAGE_COMPARE_1.get();
        Start.Request startReq = new Start.Request(member.getId(), ExamType.EVENLY, Entry.LANGUAGE);

        // when
        when(mockMemberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(mockProblemRepository.findAllByEntry(Entry.LANGUAGE)).thenReturn(FixtureFactory.getProblemList(4, Entry.LANGUAGE));
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
    @DisplayName("시험 제출 테스트")
    void testSubmitAnswer() {
        // given
        Integer problemNumber = 1;
        Integer submitAnswer = 1;
        Exam exam = FixtureFactory.getExam(member.getId(), ExamType.EVENLY, Entry.LANGUAGE);
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
        Exam exam = FixtureFactory.getExam(member.getId(), ExamType.EVENLY, Entry.LANGUAGE);
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
    @DisplayName("시험 종료 테스트")
    void testEndExam() {
        // given
        Exam exam = FixtureFactory.getExam(member.getId(), ExamType.EVENLY, Entry.LANGUAGE);

        // when
        when(mockExamRepository.findById(any())).thenReturn(Optional.of(exam));
        End.Response endRes = suit.end(exam.getId());

        // then
        assertThat(endRes.entry()).isEqualTo(Entry.LANGUAGE);
        assertThat(endRes.isFinished()).isFalse();
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
        assertThat(endRes2.isFinished()).isFalse();
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