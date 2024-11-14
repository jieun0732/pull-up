package pull_up.infra.database.repository.exam;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pull_up.infra.database.entity.Exam;
import pull_up.infra.database.entity.Member;
import pull_up.infra.database.fixture.ExamFixture;
import pull_up.infra.database.fixture.MemberFixture;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.ANY,
        connection = EmbeddedDatabaseConnection.H2)
class ExamRepositoryTest {

    @Autowired
    ExamRepository suit;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("시험 조회 시 해당 문제(AnsweredProblem) 함께 잘 가져오는지 테스트")
    void testGetExamWithAnswer() {
        // given
        Member member = MemberFixture.APPLE_USER.get();
        Exam exam = ExamFixture.MATHEMATICS.get(member);

        em.persist(member);
        em.persist(exam);
        exam.getAnswers().forEach(answeredProblem -> {
            answeredProblem.getProblem().setId(null);
            answeredProblem.setId(null);
            em.persist(answeredProblem.getProblem());
            em.persist(answeredProblem);
        });

        em.flush();
        em.clear();

        Long id = exam.getId();

        // when
        Exam examInDB = suit.findByIdWithAnswer(id).get();

        // then
        assertThat(examInDB).usingRecursiveComparison()
                .ignoringFields("member", "answers").isEqualTo(exam);

        assertThat(examInDB.getAnswers()).hasSize(5);
    }

}