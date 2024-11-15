package pull_up.infra.database.repository.answer;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pull_up.domain.problem.ProblemRepository;
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

    @BeforeEach
    void init() {
        suit.deleteAll();
        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("틀린문제 조회 시 틀린문제만 가져오는지 테스트")
    void testFindIncorrectAnswer() {
        // given
        Member member = MemberFixture.APPLE_USER.get();
        Exam exam = ExamFixture.MATHEMATICS.get(member);
        Answer correctAnswer = makeAnswer(exam, 0, true, "3");
        Answer notSolvedAnswer1 = makeAnswer(exam, 1, false, "");
        Answer notSolvedAnswer2 = makeAnswer(exam, 4, false, "");
        Answer incorrectAnswer1 = makeAnswer(exam, 2, false, "3");
        Answer incorrectAnswer2 = makeAnswer(exam, 3, false, "3");


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

    private static Answer makeAnswer(Exam exam, int index, boolean isCorrect, String chosenAnswer) {
        Answer answer = exam.getAnswers().get(index);
        answer.setIsCorrect(isCorrect);
        answer.setChosenAnswer(chosenAnswer);
        return answer;
    }

    @Test
    @DisplayName("틀린문제 상세 조회 시 문제도 같이 가져오는지 테스트")
    void testIncorrectAnswerWithProblem() {
        // given
        Member member = MemberFixture.APPLE_USER.get();
        Exam exam = ExamFixture.MATHEMATICS.get(member);

        Answer incorrectAnswer = makeAnswer(exam, 2, false, "3");

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