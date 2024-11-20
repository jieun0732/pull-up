package pull_up.infra.database.jpa.repository.problem;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pull_up.config.annotation.IntegrationTest;
import pull_up.infra.database.jpa.fixture.legacy.ProblemFixture;
import pull_up.infra.database.jpa.entity.legacy.*;
import pull_up.infra.database.jpa.repository.legacy.ProblemLRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class ProblemLRepositoryTest {

    @Autowired
    ProblemLRepository suit;

    @Autowired
    EntityManager em;

    ProblemL problemL;
    MemberL memberL;
    List<AnswerL> answerLS;
    List<ExamL> examLS;
    List<IncorrectAnswer> incorrectAnswers;
    List<MemberAnswer> memberAnswers;

    @BeforeEach
    void init() {

    }

    @Test
    @DisplayName("problem 저장 테스트")
    void testSaveProblem() {
        // given
        ProblemL problemL2 = ProblemL.of("test", "test", "test", "123", "1234", "1", "2", "3", "4", "5", "1", "qwer1234", 100, 30, 30d);

        // when
        suit.save(problemL2);

        // then
        assertThat(suit.findById(problemL2.getId()).get()).usingRecursiveComparison().isEqualTo(problemL2);
        suit.deleteAll();
    }

    @Test
    @DisplayName("problem 삭제 시 연관된 엔티티 모두 삭제되는지 확인")
    void testDeleteAllRelation() {
        // given
        memberL = MemberL.of("test", "test@example.com", false, "apple-user");
        problemL = ProblemL.of("수리", "골고루", "속력", "test123", "test1234", "1", "2", "3", "4", "5", "1", "qwer1234", 100, 30, 30d);

        examLS = List.of(ExamL.of(memberL, null, "모의고사", null, LocalDateTime.now(), null, null, 0));
        answerLS = List.of(AnswerL.of(examLS.get(0), problemL, 0L, "1", true));
        memberAnswers = List.of(MemberAnswer.of(memberL, problemL, examLS.get(0), "2", false));
        incorrectAnswers = List.of(IncorrectAnswer.of(memberL, problemL, examLS.get(0), "2", LocalDateTime.now()));

        em.persist(memberL);
        em.persist(problemL);

        answerLS.forEach(t -> em.persist(t));
        examLS.forEach(t -> em.persist(t));
        memberAnswers.forEach(t -> em.persist(t));
        incorrectAnswers.forEach(t -> em.persist(t));

        em.flush();
        em.clear();

        assertThat(em.find(ProblemL.class, problemL.getId())).isNotNull();
        assertThat(em.find(AnswerL.class, answerLS.get(0).getId())).isNotNull();
        assertThat(em.find(MemberAnswer.class, memberAnswers.get(0).getId())).isNotNull();
        assertThat(em.find(IncorrectAnswer.class, incorrectAnswers.get(0).getId())).isNotNull();

        // when
        suit.deleteAll();

        // then
        assertThat(em.find(ProblemL.class, problemL.getId())).isNull();
        assertThat(em.find(AnswerL.class, answerLS.get(0).getId())).isNull();
        assertThat(em.find(MemberAnswer.class, memberAnswers.get(0).getId())).isNull();
        assertThat(em.find(IncorrectAnswer.class, incorrectAnswers.get(0).getId())).isNull();
    }

    @Test
    @DisplayName("Problem 동적쿼리 조회 테스트")
    void testDynamicQuery() {
        // given
        suit.save(ProblemFixture.ONE.get());
        suit.save(ProblemFixture.TWO.get());
        suit.save(ProblemFixture.THREE.get());
        suit.save(ProblemFixture.FOUR.get());
        suit.save(ProblemFixture.FIVE.get());

        // when
        List<ProblemL> case1 = suit.findByEntryAndCategoryAndType("", "", "");
        List<ProblemL> case2 = suit.findByEntryAndCategoryAndType("", "", "noMatchType");
        List<ProblemL> case3 = suit.findByEntryAndCategoryAndType("", "", "속력");
        List<ProblemL> case4 = suit.findByEntryAndCategoryAndType("", "골고루", "");
        List<ProblemL> case5 = suit.findByEntryAndCategoryAndType("수리", "", "");
        List<ProblemL> case6 = suit.findByEntryAndCategoryAndType("수리", "", "속력");
        List<ProblemL> case7 = suit.findByEntryAndCategoryAndType("수리", "noMatchCategory", "속력");
        List<ProblemL> case8 = suit.findByEntryAndCategoryAndType("noMatchEntry", "골고루", "속력");
        List<ProblemL> case9 = suit.findByEntryAndCategoryAndType("수리", "골고루", "속력");
        List<ProblemL> case10 = suit.findByEntryAndCategoryAndType("수리", "골고루", "");

        // then
        assertThat(case1).hasSize(5);
        assertThat(case2).hasSize(0);
        assertThat(case3).hasSize(2);
        assertThat(case4).hasSize(5);
        assertThat(case5).hasSize(5);
        assertThat(case6).hasSize(2);
        assertThat(case7).hasSize(0);
        assertThat(case8).hasSize(0);
        assertThat(case9).hasSize(2);
        assertThat(case10).hasSize(5);
    }
}