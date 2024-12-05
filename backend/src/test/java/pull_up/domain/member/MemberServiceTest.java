package pull_up.domain.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pull_up.domain.dao.ExamRepository;
import pull_up.domain.dao.ExamsheetRepository;
import pull_up.domain.dao.MemberRepository;
import pull_up.domain.dao.ProblemRepository;
import pull_up.domain.exam.ExamType;
import pull_up.domain.member.dto.SolvedInfo;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.embedded.Problemsheet;
import pull_up.infra.database.jpa.entity.Exam;
import pull_up.infra.database.jpa.entity.Member;
import pull_up.infra.database.jpa.fixture.FixtureRepository;
import pull_up.infra.database.jpa.fixture.MemberFixture;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MemberServiceTest {

    MemberService suit;

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
        suit = new MemberService(mockMemberRepository, mockExamRepository, mockProblemRepository);

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
        when(mockProblemRepository.findAllProblemTypeAndCountExceptProblemsheetByEntry(entry)).thenReturn(problemTypesMap);
        SolvedInfo.ByEntryResponse solvedInfo = suit.getSolvedInfo(memberId, entry);

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
        when(mockProblemRepository.findAllProblemTypeAndCountExceptProblemsheetByEntry(entry)).thenReturn(problemTypesMap);
        SolvedInfo.ByEntryResponse solvedInfo = suit.getSolvedInfo(memberId, entry);

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
        when(mockProblemRepository.findAllProblemTypeAndCountExceptProblemsheetByEntry(entry)).thenReturn(problemTypesMap);
        SolvedInfo.ByEntryResponse solvedInfo = suit.getSolvedInfo(memberId, entry);

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


}
