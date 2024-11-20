package pull_up.global.entity;

import jakarta.persistence.EntityManager;
import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pull_up.config.annotation.IntegrationTest;
import pull_up.domain.dao.MemberRepository;
import pull_up.infra.database.jpa.entity.Member;
import pull_up.infra.database.jpa.fixture.MemberFixture;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class BaseEntityTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("저장 시 생성시간 함께 저장하는지 확인")
    void testCreateAt() {
        // given
        Member member = MemberFixture.APPLE_USER.get();
        member.setId(null);

        // when
        em.persist(member);
        em.flush();
        em.clear();
        member = memberRepository.findById(member.getId()).get();

        // then
        assertThat(member.getId()).isNotNull();
        assertThat(member.getCreatedTime()).isCloseTo(LocalDateTime.now(), new TemporalUnitWithinOffset(1, ChronoUnit.SECONDS));
        assertThat(member.getUpdatedTime()).isCloseTo(LocalDateTime.now(), new TemporalUnitWithinOffset(1, ChronoUnit.SECONDS));
    }
}