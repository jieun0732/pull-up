package pull_up.infra.database.repository.member;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pull_up.infra.database.entity.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.ANY,
        connection = EmbeddedDatabaseConnection.H2)
class MemberRepositoryTest {

    @Autowired
    MemberRepository suit;

    @Autowired
    EntityManager em;

    // fixtures
    Problem problem;
    Member member;
    AnsweredProblem answeredProblem;
    List<Exam> exams;
    List<IncorrectAnswer> incorrectAnswers;
    List<MemberAnswer> memberAnswers;


    @BeforeEach
    void init() {
        problem = Problem.of("수리",
                "골고루", "속력", "test123", "test1234", "1", "2", "3", "4", "5", "1", "qwer1234", 100, 30, 30d);
        member = Member.of("test", "test@example.com", false, "apple-user");

        exams = List.of(Exam.of(member, null, "모의고사", null, LocalDateTime.now(), null, null, 0));
        memberAnswers = List.of(MemberAnswer.of(member, problem, exams.get(0), "2", false));
        incorrectAnswers = List.of(IncorrectAnswer.of(member, problem, exams.get(0), "2", LocalDateTime.now()));
        answeredProblem = AnsweredProblem.of(exams.get(0), problem,0L,"1", true);

        member.setExamList(exams);
        member.setIncorrectAnswers(incorrectAnswers);
        member.setMemberAnswers(memberAnswers);

        em.persist(answeredProblem);
        em.persist(problem);
        em.persist(member);
        exams.forEach(t -> em.persist(t));
        memberAnswers.forEach(t -> em.persist(t));
        incorrectAnswers.forEach(t -> em.persist(t));

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("회원 삭제 시 관련 테이블 한번에 삭제하는지 테스트")
    void testDeleteRelatedTable() {
        // given
        assertThat(em.find(Member.class, member.getId())).isNotNull();
        assertThat(em.find(Exam.class, exams.get(0).getId())).isNotNull();
        assertThat(em.find(IncorrectAnswer.class, incorrectAnswers.get(0).getId())).isNotNull();
        assertThat(em.find(MemberAnswer.class, memberAnswers.get(0).getId())).isNotNull();

        // when
        suit.delete(member);

        // then
        assertThat(em.find(Member.class, member.getId())).isNull();
        assertThat(em.find(Exam.class, exams.get(0).getId())).isNull();
        assertThat(em.find(IncorrectAnswer.class, incorrectAnswers.get(0).getId())).isNull();
        assertThat(em.find(MemberAnswer.class, memberAnswers.get(0).getId())).isNull();
    }
}