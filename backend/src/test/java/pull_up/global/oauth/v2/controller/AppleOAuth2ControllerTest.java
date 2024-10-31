package pull_up.global.oauth.v2.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.view.RedirectView;
import pull_up.global.oauth.v2.dto.OAuth2LoginResponseDto;

import static org.assertj.core.api.Assertions.assertThat;

class AppleOAuth2ControllerTest {

    AppleOAuth2Controller suit;

    @BeforeEach
    void init() {
        suit = new AppleOAuth2Controller(null);
    }

    @Test
    @DisplayName("애플 로그인 시 회원가입 여부 추가")
    void testFirstLoginUser() {
        // given
        OAuth2LoginResponseDto user = OAuth2LoginResponseDto.builder()
                .firstLogin(true)
                .email("test@example.com")
                .name("leaf")
                .memberId(1L)
                .provider("apple")
                .build();

        // when
        RedirectView redirectView = suit.setRedirect(user);

        // then
        assertThat(redirectView.getAttributesMap()).hasFieldOrProperty("firstLogin")
                .extractingByKey("firstLogin").isEqualTo("true");
    }
}