package pull_up.domain.problem;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pull_up.infra.database.entity.*;
import pull_up.infra.database.entity.legacy.IncorrectAnswer;
import pull_up.infra.database.entity.legacy.MemberAnswer;
import pull_up.infra.database.fixture.ProblemFixture;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.ANY,
        connection = EmbeddedDatabaseConnection.H2)
class ProblemRepositoryTest {

    @Autowired
    @Qualifier(value = "problemRepository")
    ProblemRepository suit;

    @Autowired
    EntityManager em;

    Problem problem;
    Member member;
    List<Answer> answers;
    List<Exam> exams;
    List<IncorrectAnswer> incorrectAnswers;
    List<MemberAnswer> memberAnswers;

    @BeforeEach
    void init() {

    }

    @Test
    @DisplayName("problem 저장 테스트")
    void testSaveProblem() {
        // given
        Problem problem2 = Problem.of("test", "test", "test", "123", "1234", "1", "2", "3", "4", "5", "1", "qwer1234", 100, 30, 30d);

        // when
        suit.save(problem2);

        // then
        assertThat(suit.findById(problem2.getId()).get()).usingRecursiveComparison().isEqualTo(problem2);
        suit.deleteAll();
    }

    @Test
    @DisplayName("problem 삭제 시 연관된 엔티티 모두 삭제되는지 확인")
    void testDeleteAllRelation() {
        // given
        member = Member.of("test", "test@example.com", false, "apple-user");
        problem = Problem.of("수리", "골고루", "속력", "test123", "test1234", "1", "2", "3", "4", "5", "1", "qwer1234", 100, 30, 30d);

        exams = List.of(Exam.of(member, null, "모의고사", null, LocalDateTime.now(), null, null, 0));
        answers = List.of(Answer.of(exams.get(0), problem, 0L, "1", true));
        memberAnswers = List.of(MemberAnswer.of(member, problem, exams.get(0), "2", false));
        incorrectAnswers = List.of(IncorrectAnswer.of(member, problem, exams.get(0), "2", LocalDateTime.now()));

        em.persist(member);
        em.persist(problem);

        answers.forEach(t -> em.persist(t));
        exams.forEach(t -> em.persist(t));
        memberAnswers.forEach(t -> em.persist(t));
        incorrectAnswers.forEach(t -> em.persist(t));

        em.flush();
        em.clear();

        assertThat(em.find(Problem.class, problem.getId())).isNotNull();
        assertThat(em.find(Answer.class, answers.get(0).getId())).isNotNull();
        assertThat(em.find(MemberAnswer.class, memberAnswers.get(0).getId())).isNotNull();
        assertThat(em.find(IncorrectAnswer.class, incorrectAnswers.get(0).getId())).isNotNull();

        // when
        suit.deleteAll();

        // then
        assertThat(em.find(Problem.class, problem.getId())).isNull();
        assertThat(em.find(Answer.class, answers.get(0).getId())).isNull();
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
        List<Problem> case1 = suit.findByEntryAndCategoryAndType("", "", "");
        List<Problem> case2 = suit.findByEntryAndCategoryAndType("", "", "noMatchType");
        List<Problem> case3 = suit.findByEntryAndCategoryAndType("", "", "속력");
        List<Problem> case4 = suit.findByEntryAndCategoryAndType("", "골고루", "");
        List<Problem> case5 = suit.findByEntryAndCategoryAndType("수리", "", "");
        List<Problem> case6 = suit.findByEntryAndCategoryAndType("수리", "", "속력");
        List<Problem> case7 = suit.findByEntryAndCategoryAndType("수리", "noMatchCategory", "속력");
        List<Problem> case8 = suit.findByEntryAndCategoryAndType("noMatchEntry", "골고루", "속력");
        List<Problem> case9 = suit.findByEntryAndCategoryAndType("수리", "골고루", "속력");
        List<Problem> case10 = suit.findByEntryAndCategoryAndType("수리", "골고루", "");

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