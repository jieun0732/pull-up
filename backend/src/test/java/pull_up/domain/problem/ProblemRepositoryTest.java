package pull_up.domain.problem;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pull_up.infra.database.entity.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

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
    List<ExamProblem> examProblems;
    List<ExamInformation> examInformations;
    List<IncorrectAnswer> incorrectAnswers;
    List<MemberAnswer> memberAnswers;

    @BeforeEach
    void init() {
        member = Member.of("test", "test@example.com", false, "apple-user");
        problem = Problem.of("수리", "골고루", "속력", "test123", "test1234", "1", "2", "3", "4", "5", "1", "qwer1234", 100, 30, 30d);

        examInformations = List.of(ExamInformation.of(member, null, "모의고사", null, LocalDateTime.now(), null, null, 0));
        examProblems = List.of(ExamProblem.of(examInformations.get(0), problem,0L,"1", true));
        memberAnswers = List.of(MemberAnswer.of(member, problem, examInformations.get(0), "2", false));
        incorrectAnswers = List.of(IncorrectAnswer.of(member, problem, examInformations.get(0), "2", LocalDateTime.now()));

        em.persist(member);
        em.persist(problem);
        examProblems.forEach(t -> em.persist(t));
        examInformations.forEach(t -> em.persist(t));
        memberAnswers.forEach(t -> em.persist(t));
        incorrectAnswers.forEach(t -> em.persist(t));

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("problem 삭제 시 연관된 엔티티 모두 삭제되는지 확인")
    void testDeleteAllRelation() {
        // given
        assertThat(em.find(Problem.class,problem.getId())).isNotNull();
        assertThat(em.find(ExamProblem.class,examProblems.get(0).getId())).isNotNull();
        assertThat(em.find(MemberAnswer.class,memberAnswers.get(0).getId())).isNotNull();
        assertThat(em.find(IncorrectAnswer.class,incorrectAnswers.get(0).getId())).isNotNull();

        // when
        suit.deleteAll();

        // then
        assertThat(em.find(Problem.class,problem.getId())).isNull();
        assertThat(em.find(ExamProblem.class,examProblems.get(0).getId())).isNull();
        assertThat(em.find(MemberAnswer.class,memberAnswers.get(0).getId())).isNull();
        assertThat(em.find(IncorrectAnswer.class,incorrectAnswers.get(0).getId())).isNull();
    }
}