package pull_up.domain.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pull_up.config.annotation.IntegrationTest;
import pull_up.domain.exam.ExamType;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.Exam;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class ExamRepositoryTest {

    @Autowired
    ExamRepository suit;

    @Test
    @DisplayName("사용자 ID, Entry 로 전체 시험 조회하기")
    void testFindAllByMemberIdAndEntry() {
        // given
        Long memberId = 1L;
        Entry entry = Entry.MATH;

        // when
        Map<String, Exam> exams = suit.findAllEvenlyAndProblemTypeExamMap(memberId, entry);

        // then
        assertThat(exams).hasSize(2);
        assertThat(exams).hasFieldOrProperty(ExamType.EVENLY.name());
        assertThat(exams).hasFieldOrProperty("속력");
    }
}