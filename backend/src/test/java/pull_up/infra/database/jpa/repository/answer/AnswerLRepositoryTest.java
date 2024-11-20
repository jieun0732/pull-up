package pull_up.infra.database.jpa.repository.answer;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pull_up.config.annotation.IntegrationTest;
import pull_up.infra.database.jpa.entity.legacy.AnswerL;
import pull_up.infra.database.jpa.entity.legacy.ExamL;
import pull_up.infra.database.jpa.entity.legacy.MemberL;
import pull_up.infra.database.jpa.fixture.legacy.ExamFixture;
import pull_up.infra.database.jpa.fixture.legacy.MemberFixture;
import pull_up.infra.database.jpa.repository.answer.AnswerRepositoryL;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class AnswerLRepositoryTest {

    @Autowired
    AnswerRepositoryL suit;

    @Autowired
    EntityManager em;

    MemberL memberL;
    ExamL examL;
    AnswerL correctAnswerL;
    AnswerL notSolvedAnswerL1;
    AnswerL notSolvedAnswerL2;
    AnswerL incorrectAnswerL1;
    AnswerL incorrectAnswerL2;


    private static AnswerL makeAnswer(ExamL examL, int index, boolean isCorrect, String chosenAnswer) {
        AnswerL answerL = examL.getAnswerLS().get(index);
        answerL.setIsCorrect(isCorrect);
        answerL.setChosenAnswer(chosenAnswer);
        answerL.setIsSolved(!chosenAnswer.isEmpty());
        return answerL;
    }

    @BeforeEach
    void init() {
        suit.deleteAll();

        memberL = MemberFixture.APPLE_USER.get();
        examL = ExamFixture.MATHEMATICS.get(memberL);
        correctAnswerL = makeAnswer(examL, 0, true, "3");
        notSolvedAnswerL1 = makeAnswer(examL, 1, false, "");
        notSolvedAnswerL2 = makeAnswer(examL, 4, false, "");
        incorrectAnswerL1 = makeAnswer(examL, 2, false, "3");
        incorrectAnswerL2 = makeAnswer(examL, 3, false, "3");


        em.persist(memberL);
        em.persist(examL);
        examL.getAnswerLS().forEach(answer -> {
            answer.getProblemL().setId(null);
            answer.setId(null);
            em.persist(answer.getProblemL());
            em.persist(answer);
        });

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("틀린문제 조회 시 틀린문제만 가져오는지 테스트")
    void testFindIncorrectAnswer() {
        // given

        // when
        List<AnswerL> incorrectAnswerLS = suit.findIncorrectAnswersByMemberId(memberL.getId());

        // then
        assertThat(incorrectAnswerLS).hasSize(2)
                .anySatisfy(answer -> assertThat(answer.getId()).isEqualTo(incorrectAnswerL1.getId()))
                .anySatisfy(answer -> assertThat(answer.getId()).isEqualTo(incorrectAnswerL2.getId()));
    }

    @Test
    @DisplayName("푼 문제 조회 시 잘 가져오는지 테스트")
    void testGetSolvedProblem() {
        // given
        Long correctId = correctAnswerL.getId();
        Long incorrectId = incorrectAnswerL1.getId();
        Long notSolvedId = notSolvedAnswerL1.getId();

        // when
        correctAnswerL = suit.findByIdWithProblem(correctId, true).get();
        AnswerL incorrectAnswerL = suit.findByIdWithProblem(incorrectId, true).get();
        Optional<AnswerL> notSolvedAnswer = suit.findByIdWithProblem(notSolvedId, true);

        // then
        assertThat(correctAnswerL.getChosenAnswer()).isEqualTo("3");
        assertThat(correctAnswerL.getProblemL().getAnswer()).isEqualTo("3");
        assertThat(correctAnswerL.getIsCorrect()).isTrue();
        assertThat(incorrectAnswerL.getChosenAnswer()).isEqualTo("3");
        assertThat(incorrectAnswerL.getProblemL().getAnswer()).isEqualTo("2");
        assertThat(incorrectAnswerL.getIsCorrect()).isFalse();
        assertThat(notSolvedAnswer).isEmpty();
    }

    @Test
    @DisplayName("틀린문제 상세 조회 시 문제도 같이 가져오는지 테스트")
    void testIncorrectAnswerWithProblem() {
        // given
        MemberL memberL = MemberFixture.APPLE_USER.get();
        ExamL examL = ExamFixture.MATHEMATICS.get(memberL);

        AnswerL incorrectAnswerL = makeAnswer(examL, 2, false, "3");

        em.persist(memberL);
        em.persist(examL);
        examL.getAnswerLS().forEach(answeredProblem -> {
            answeredProblem.getProblemL().setId(null);
            answeredProblem.setId(null);
            em.persist(answeredProblem.getProblemL());
            em.persist(answeredProblem);
        });

        em.flush();
        em.clear();

        // when
        AnswerL answerLInDB = suit.findByIdWithProblem(incorrectAnswerL.getId()).get();

        // then
//        assertThat(answerLInDB.getProblemL()).usingRecursiveComparison()
//                .ignoringFields("answers", "incorrectAnswers", "memberAnswers")
//                .isEqualTo(incorrectAnswerL.getProblemL());
    }
}