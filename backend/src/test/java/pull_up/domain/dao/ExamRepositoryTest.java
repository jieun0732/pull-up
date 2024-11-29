package pull_up.domain.dao;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pull_up.config.annotation.IntegrationTest;
import pull_up.domain.exam.ExamType;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.Answer;
import pull_up.infra.database.jpa.entity.Exam;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class ExamRepositoryTest {

    @Autowired
    ExamRepository suit;

    @Autowired
    EntityManager em;

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

    @Test
    @DisplayName("시험 삭제 시 문제도 함께 삭제되는지 테스트")
    void testDeleteExamWithProblem() {
        // given
        Exam exam = suit.findById(1L).get();
        List<Answer> answers = exam.getAnswers();

        // when
        Answer answer = em.find(Answer.class, answers.get(0).getId());
        suit.delete(exam);
        Answer deletedAnswer = em.find(Answer.class, answers.get(0).getId());

        // then
        assertThat(answer).isNotNull();
        assertThat(deletedAnswer).isNull();
    }
}