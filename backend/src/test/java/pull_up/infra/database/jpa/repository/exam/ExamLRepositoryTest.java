package pull_up.infra.database.jpa.repository.exam;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pull_up.config.annotation.IntegrationTest;
import pull_up.infra.database.jpa.entity.legacy.ExamL;
import pull_up.infra.database.jpa.entity.legacy.MemberL;
import pull_up.infra.database.jpa.fixture.legacy.ExamFixture;
import pull_up.infra.database.jpa.fixture.legacy.MemberFixture;
import pull_up.infra.database.jpa.repository.legacy.ExamLRepository;

@IntegrationTest
class ExamLRepositoryTest {

    @Autowired
    ExamLRepository suit;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("시험 조회 시 해당 문제(AnsweredProblem) 함께 잘 가져오는지 테스트")
    void testGetExamWithAnswer() {
        // given
        MemberL memberL = MemberFixture.APPLE_USER.get();
        ExamL examL = ExamFixture.MATHEMATICS.get(memberL);

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

        Long id = examL.getId();

        // when
        ExamL examLInDB = suit.findByIdWithAnswer(id).get();

        // then
//        assertThat(examLInDB).usingRecursiveComparison()
//                .ignoringFields("member", "answers").isEqualTo(examL);

//        assertThat(examLInDB.getAnswerLS()).hasSize(5);
    }

}