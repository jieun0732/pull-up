package pull_up.domain.member.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pull_up.domain.exam.dto.SolvedInfo;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.Exam;
import pull_up.infra.database.jpa.fixture.FixtureRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class SolvedInfoTest {

    @Test
    @DisplayName("Map -> ProblemTypeInfo 변환 테스트")
    void testToList() {
        // given
        Map<String, Exam> problemMap = new HashMap<>();
        String problemType1 = "testProblemType1";
        String problemType2 = "testProblemType2";
        problemMap.put(problemType1, FixtureRepository.getEmptyProblemTypeExam(Entry.MATH, problemType1));
        problemMap.put(problemType2, FixtureRepository.getEmptyProblemTypeExam(Entry.MATH, problemType2));

        // when
        List<SolvedInfo.Response.ProblemTypeInfo> list = SolvedInfo.Response.ProblemTypeInfo.toList(problemMap);

        // then
        assertThat(list).hasSize(2);
        assertThat(list).allSatisfy(problemTypeInfo -> assertThat(problemTypeInfo.isStarted()).isFalse());
        assertThat(list).anySatisfy(problemTypeInfo -> assertThat(problemTypeInfo.problemType()).isEqualTo(problemType1))
                .anySatisfy(problemTypeInfo -> assertThat(problemTypeInfo.problemType()).isEqualTo(problemType2));
    }

    @Test
    @DisplayName("Exams -> SolvedInfo 변환 테스트 : [case1] 아무 문제도 조회되지 않았을 때")
    void testToDto() {
        Entry entry = Entry.MATH;
        Map<String, Integer> problemTypesMap = FixtureRepository.getProblemTypesMap(entry, "용액의 농도");

        // given
        List<Exam> exams = new ArrayList<>();

        // when
        SolvedInfo.Response response = SolvedInfo.Response.toDto(entry, exams, problemTypesMap);

        // then
        assertThat(response.isEvenlyExamStarted()).isFalse();
        assertThat(response.isEvenlyExamFinished()).isFalse();
        assertThat(response.problemTypeCount()).isEqualTo(1);
        assertThat(response.problemTypeInfos()).allSatisfy(problemTypeInfo -> assertThat(problemTypeInfo.isStarted()).isFalse());
    }

    @Test
    @DisplayName("Exams -> SolvedInfo 변환 테스트 : [case2] 유형별 문제 다 풀었을 때")
    void testToDto2() {
        Entry entry = Entry.MATH;
        Map<String, Integer> problemTypesMap = FixtureRepository.getProblemTypesMap(entry, "용액의 농도");

        // given
        List<Exam> exams = new ArrayList<>();
        Exam problemTypeExam = FixtureRepository.getProblemTypeExam(1L, Entry.MATH, "용액의 농도");
        problemTypeExam.submit(1, 1);
        exams.add(problemTypeExam);

        // when
        SolvedInfo.Response response = SolvedInfo.Response.toDto(entry, exams, problemTypesMap);

        // then
        assertThat(response.isEvenlyExamStarted()).isFalse();
        assertThat(response.isEvenlyExamFinished()).isFalse();
        assertThat(response.problemTypeCount()).isEqualTo(1);
        assertThat(response.problemTypeInfos()).allSatisfy(problemTypeInfo -> {
            assertThat(problemTypeInfo.isStarted()).isTrue();
            assertThat(problemTypeInfo.solvedProblemCount()).isEqualTo(1);
            });
    }

    @Test
    @DisplayName("Exams -> SolvedInfo 변환 테스트 : [case3] 골고루 문제 / 유형별 문제 다 풀었을 때")
    void testToDto3() {
        Entry entry = Entry.MATH;
        Map<String, Integer> problemTypesMap = FixtureRepository.getProblemTypesMap(entry, "용액의 농도");

        // given
        List<Exam> exams = new ArrayList<>();
        Exam evenlyExam = FixtureRepository.getEvenlyExam(1L, Entry.MATH);
        Exam problemTypeExam = FixtureRepository.getProblemTypeExam(1L, Entry.MATH, "용액의 농도");
        evenlyExam.submit(1, 1);
        evenlyExam.submit(2, 1);
        problemTypeExam.submit(1, 1);
        exams.add(evenlyExam);
        exams.add(problemTypeExam);

        // when
        SolvedInfo.Response response = SolvedInfo.Response.toDto(entry, exams, problemTypesMap);

        // then
        assertThat(response.isEvenlyExamStarted()).isTrue();
        assertThat(response.isEvenlyExamFinished()).isTrue();
        assertThat(response.problemTypeCount()).isEqualTo(1);
        assertThat(response.problemTypeInfos()).allSatisfy(problemTypeInfo -> {
            assertThat(problemTypeInfo.isStarted()).isTrue();
            assertThat(problemTypeInfo.solvedProblemCount()).isEqualTo(1);
        });
    }
}