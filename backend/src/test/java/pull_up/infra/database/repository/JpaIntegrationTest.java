package pull_up.infra.database.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pull_up.config.annotation.IntegrationTest;
import pull_up.domain.answer.AnswerRepository;
import pull_up.domain.exam.ExamRepository;
import pull_up.domain.member.MemberRepository;
import pull_up.domain.problem.ProblemRepository;
import pull_up.infra.database.entity.Answer;
import pull_up.infra.database.entity.Exam;
import pull_up.infra.database.entity.Member;
import pull_up.infra.database.entity.Problem;
import pull_up.infra.database.fixture.*;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
public class JpaIntegrationTest {

    @Autowired
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProblemRepository problemRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    ExamRepository examRepository;

    @Test
    @DisplayName("mysql 연결 테스트")
    void testMysqlConnection() {
        assertThat(em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(MemberRepository.class)).isTrue();
        assertThat(em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(ProblemRepository.class)).isTrue();
        assertThat(em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(AnswerRepository.class)).isTrue();
        assertThat(em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(ExamRepository.class)).isTrue();
    }

    @Test
    @DisplayName("기본 멤버 데이터 조회")
    void testBasicMemberData() {
        List<Member> all = memberRepository.findAll();
        Fixture<Member>[] fixtures = MemberFixture.values();
        assertDBandFixture(all, Arrays.stream(fixtures).toList());
    }

    @Test
    @DisplayName("기본 문제 데이터 조회")
    void testBasicProblemData() {
        List<Problem> all = problemRepository.findAll();
        Fixture<Problem>[] fixtures = ProblemFixture.values();
        assertDBandFixture(all, Arrays.stream(fixtures).toList());
    }

    private <T> void assertDBandFixture (List<T> all, List<Fixture<T>> fixtures) {
        assertThat(all).hasSize(fixtures.size());
        for (Fixture<T> fixture : fixtures) {
            assertThat(all).anySatisfy(entity -> assertThat(entity).usingRecursiveComparison().isEqualTo(fixture.get()));
        }
    }
}
