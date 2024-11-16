package pull_up.domain.exam;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pull_up.api.exam.evenly.dto.Start;
import pull_up.domain.member.MemberRepository;
import pull_up.domain.problem.Entry;
import pull_up.domain.problem.ProblemRepository;
import pull_up.infra.database.entity.Member;
import pull_up.infra.database.entity.Problem;
import pull_up.infra.database.fixture.FixtureFactory;
import pull_up.infra.database.fixture.MemberFixture;
import pull_up.infra.database.fixture.ProblemFixture;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class EvenlyExamServiceTest {

    EvenlyExamService suit;

    ExamRepository mockExamRepository;
    MemberRepository mockMemberRepository;
    ProblemRepository mockProblemRepository;

    @BeforeEach
    void init() {
        mockExamRepository = mock(ExamRepository.class);
        mockMemberRepository = mock(MemberRepository.class);
        mockProblemRepository = mock(ProblemRepository.class);
        suit = new EvenlyExamService(mockExamRepository, null, mockMemberRepository, mockProblemRepository);
    }

    @Test
    @DisplayName("시험 시작 테스트")
    void testStartExam() {
        // given
        Member member = MemberFixture.APPLE_USER.get();
        Start.Request startReq = new Start.Request(member.getId(), ExamType.EVENLY, Entry.LANGUAGE);
        Problem problem1 = ProblemFixture.LANGUAGE_COMPARE_1.get();

        // when
        when(mockMemberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(mockProblemRepository.findAllByEntry(Entry.LANGUAGE)).thenReturn(FixtureFactory.getProblemList(4, Entry.LANGUAGE));
        Start.Response startRes = suit.start(startReq);

        // then
        assertThat(startRes.problemId()).isEqualTo(9L);
        assertThat(startRes.totalProblemCount()).isEqualTo(2);
        assertThat(startRes.leftProblemCount()).isEqualTo(1);
        assertThat(startRes.problemNumber()).isEqualTo(1);
        assertThat(startRes.entry()).isEqualTo(problem1.getEntry());
        assertThat(startRes.problemType()).isEqualTo(problem1.getProblemType());
        assertThat(startRes.question()).isEqualTo(problem1.getQuestionAsString());
        assertThat(startRes.example()).isEqualTo(problem1.getExampleAsString());
        assertThat(startRes.choices()).contains(problem1.getChoice1(), problem1.getChoice2(), problem1.getChoice3(), problem1.getChoice4(), problem1.getChoice5());
    }

}