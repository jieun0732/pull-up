package pull_up.infra.database.jpa.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pull_up.infra.database.jpa.fixture.MemberFixture;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    @Test
    @DisplayName("사용자 이메일 숨김처리 테스트")
    void testPrivateEmail() {
        // given
        Member member = MemberFixture.APPLE_EMAIL_CONCEALED_USER.get();

        // when
        String privateEmail = member.getPrivateEmail();

        // then
        assertThat(privateEmail).isEqualTo("CONCEALED_EMAIL");
    }

}