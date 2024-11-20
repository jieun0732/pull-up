package pull_up.api.member.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pull_up.api.member.dto.MemberScoreDto;
import pull_up.config.annotation.IntegrationTest;
import pull_up.domain.deprecated.MemberServiceL;
import pull_up.infra.database.jpa.entity.legacy.MemberL;
import pull_up.infra.database.jpa.repository.member.MemberRepositoryL;
import pull_up.domain.auth.SNSProvider;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class MemberLServiceTest {

    @Autowired
    MemberRepositoryL memberRepositoryL;

    MemberServiceL suit;

    MemberL memberL;

    @BeforeEach
    void init() {
        suit = new MemberServiceL(memberRepositoryL);
        memberL = MemberL.of("test", "test@privaterelay.appleid.com", false, SNSProvider.APPLE.getRole());
        memberRepositoryL.save(memberL);
    }

    @Test
    @DisplayName("사용자 정보 조회 시 가려진 이메일 테스트")
    void testPrivateEmail() {
        // given

        // when
        MemberScoreDto dto = suit.getMemberById(memberL.getId());

        // then
        assertThat(dto.email()).isEqualTo("CONCEALED_EMAIL");
    }
}