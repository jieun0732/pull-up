package pull_up.global.security.v2.util;

import jakarta.servlet.http.Cookie;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import pull_up.domain.auth.auth.AuthError;
import pull_up.domain.auth.auth.AuthException;
import pull_up.global.security.util.CookieUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CookieUtilTest {

    CookieUtil suit;

    String domain = "https://example.com";
    Integer maxAge = 1234;
    String path = "/";

    String token;
    Cookie goodCookie;
    @BeforeEach
    void init() {
        suit = new CookieUtil();
        ReflectionTestUtils.setField(suit, "domain", domain);
        ReflectionTestUtils.setField(suit, "maxAge", maxAge);
        ReflectionTestUtils.setField(suit, "path", path);

        token = "1234";
        goodCookie = new Cookie("accessToken", token);
        goodCookie.setDomain(domain);
        goodCookie.setHttpOnly(true);
        goodCookie.setMaxAge(maxAge);
        goodCookie.setSecure(true);
        goodCookie.setPath(path);
        goodCookie.setAttribute("SameSite", "None");
    }

    @Test
    @DisplayName("secret cookie 설정 확인")
    void testSecretCookieConfig() {
        // given

        // when
        Cookie cookie = suit.getSecureCookie(token);

        // then
        assertThat(cookie).usingRecursiveComparison()
                .isEqualTo(goodCookie);
    }

    @Test
    @DisplayName("Access Token 가져오기")
    void testGetAccessToken() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletRequest invalidRequest = new MockHttpServletRequest();
        request.setCookies(goodCookie);

        // when
        String accessToken = suit.getAccessToken(request);

        // then
        assertThat(accessToken).isEqualTo(token);

        // when2
        AbstractThrowableAssert<?, ? extends Throwable> exception = assertThatThrownBy(() -> suit.getAccessToken(invalidRequest));

        // then2
        exception.isInstanceOf(AuthException.class).hasMessage(AuthError.NO_ACCESS_TOKEN_IN_COOKIES.getMessage());
    }
}