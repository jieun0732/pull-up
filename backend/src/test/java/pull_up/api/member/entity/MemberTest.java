package pull_up.api.member.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @Test
    @DisplayName("애플 로그인 시 이메일 미공개 처리정책 시 기본값으로 변경하는지 확인")
    void testNotUseEmail() {
        // given
        String privateEmail = "test1234@privaterelay.appleid.com";

        // when
        Member member = Member.of("test", privateEmail, false, "apple-user");

        // then
        Assertions.assertThat(member.getEmail()).isEqualTo("CONCEALED_EMAIL");
    }
}