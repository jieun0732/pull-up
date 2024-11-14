package pull_up.infra.database.repository.answer;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pull_up.infra.database.entity.Answer;
import pull_up.infra.database.entity.Exam;
import pull_up.infra.database.entity.Member;
import pull_up.infra.database.fixture.ExamFixture;
import pull_up.infra.database.fixture.MemberFixture;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.ANY,
        connection = EmbeddedDatabaseConnection.H2)
class AnswerRepositoryTest {

    @Autowired
    AnswerRepository suit;

    @Autowired
    EntityManager em;
    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @DisplayName("틀린문제 조회 시 틀린문제만 가져오는지 테스트")
    void testFindIncorrectAnswer() {
        // given
        answerRepository.deleteAll();

        Member member = MemberFixture.APPLE_USER.get();
        Exam exam = ExamFixture.MATHEMATICS.get(member);
        Answer correctAnswer = exam.getAnswers().get(0);
        correctAnswer.setIsCorrect(true);
        correctAnswer.setChosenAnswer("3");

        Answer incorrectAnswer1 = exam.getAnswers().get(2);
        incorrectAnswer1.setIsCorrect(false);
        incorrectAnswer1.setChosenAnswer("3");

        Answer incorrectAnswer2 = exam.getAnswers().get(3);
        incorrectAnswer2.setIsCorrect(false);
        incorrectAnswer2.setChosenAnswer("3");

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

        // when
        List<Answer> incorrectAnswers = suit.findIncorrectAnswersByMemberId(member.getId());

        // then
        assertThat(incorrectAnswers).hasSize(2)
                .anySatisfy(answer -> assertThat(answer.getId()).isEqualTo(incorrectAnswer1.getId()))
                .anySatisfy(answer -> assertThat(answer.getId()).isEqualTo(incorrectAnswer2.getId()));
    }

    @Test
    @DisplayName("틀린문제 상세 조회 시 문제도 같이 가져오는지 테스트")
    void testIncorrectAnswerWithProblem() {
        // given
        answerRepository.deleteAll();

        Member member = MemberFixture.APPLE_USER.get();
        Exam exam = ExamFixture.MATHEMATICS.get(member);

        Answer incorrectAnswer = exam.getAnswers().get(2);
        incorrectAnswer.setIsCorrect(false);
        incorrectAnswer.setChosenAnswer("3");

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

        // when
        Answer answerInDB = suit.findByIdWithProblem(incorrectAnswer.getId()).get();

        // then
        assertThat(answerInDB.getProblem()).usingRecursiveComparison()
                .ignoringFields("answers", "incorrectAnswers", "memberAnswers")
                .isEqualTo(incorrectAnswer.getProblem());
    }
}