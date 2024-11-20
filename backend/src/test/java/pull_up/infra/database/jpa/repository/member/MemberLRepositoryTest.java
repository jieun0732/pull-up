package pull_up.infra.database.jpa.repository.member;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pull_up.config.annotation.IntegrationTest;
import pull_up.infra.database.jpa.entity.legacy.*;
import pull_up.infra.database.jpa.repository.member.MemberRepositoryL;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@IntegrationTest
class MemberLRepositoryTest {

    @Autowired
    MemberRepositoryL suit;

    @Autowired
    EntityManager em;

    // fixtures
    ProblemL problemL;
    MemberL memberL;
    AnswerL answerL;
    List<ExamL> examLS;
    List<IncorrectAnswer> incorrectAnswers;
    List<MemberAnswer> memberAnswers;


    @BeforeEach
    void init() {
        problemL = ProblemL.of("수리",
                "골고루", "속력", "test123", "test1234", "1", "2", "3", "4", "5", "1", "qwer1234", 100, 30, 30d);
        memberL = MemberL.of("test", "test@example.com", false, "apple-user");

        examLS = List.of(ExamL.of(memberL, null, "모의고사", null, LocalDateTime.now(), null, null, 0));
        memberAnswers = List.of(MemberAnswer.of(memberL, problemL, examLS.get(0), "2", false));
        incorrectAnswers = List.of(IncorrectAnswer.of(memberL, problemL, examLS.get(0), "2", LocalDateTime.now()));
        answerL = AnswerL.of(examLS.get(0), problemL,0L,"1", true);

        memberL.setExamLList(examLS);
        memberL.setIncorrectAnswers(incorrectAnswers);
        memberL.setMemberAnswers(memberAnswers);

        em.persist(answerL);
        em.persist(problemL);
        em.persist(memberL);
        examLS.forEach(t -> em.persist(t));
        memberAnswers.forEach(t -> em.persist(t));
        incorrectAnswers.forEach(t -> em.persist(t));

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("회원 삭제 시 관련 테이블 한번에 삭제하는지 테스트")
    void testDeleteRelatedTable() {
        // given
        assertThat(em.find(MemberL.class, memberL.getId())).isNotNull();
        assertThat(em.find(ExamL.class, examLS.get(0).getId())).isNotNull();
        assertThat(em.find(IncorrectAnswer.class, incorrectAnswers.get(0).getId())).isNotNull();
        assertThat(em.find(MemberAnswer.class, memberAnswers.get(0).getId())).isNotNull();

        // when
        suit.delete(memberL);

        // then
        assertThat(em.find(MemberL.class, memberL.getId())).isNull();
        assertThat(em.find(ExamL.class, examLS.get(0).getId())).isNull();
        assertThat(em.find(IncorrectAnswer.class, incorrectAnswers.get(0).getId())).isNull();
        assertThat(em.find(MemberAnswer.class, memberAnswers.get(0).getId())).isNull();
    }
}