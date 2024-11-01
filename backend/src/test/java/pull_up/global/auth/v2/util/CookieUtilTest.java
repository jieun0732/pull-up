package pull_up.global.auth.v2.util;

import jakarta.servlet.http.Cookie;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class CookieUtilTest {

    CookieUtil suit;

    String domain = "https://example.com";
    Integer maxAge = 1234;
    String path = "/";


    @BeforeEach
    void init() {
        suit = new CookieUtil();
        ReflectionTestUtils.setField(suit, "domain", domain);
        ReflectionTestUtils.setField(suit, "maxAge", maxAge);
        ReflectionTestUtils.setField(suit, "path", path);
    }

    @Test
    @DisplayName("secret cookie 설정 확인")
    void testSecretCookieConfig() {
        // given
        String token = "1234";
        Cookie goodCookie = new Cookie("accessToken", token);
        goodCookie.setDomain(domain);
        goodCookie.setHttpOnly(true);
        goodCookie.setMaxAge(maxAge);
        goodCookie.setSecure(true);
        goodCookie.setPath(path);
        goodCookie.setAttribute("SameSite", "None");

        // when
        Cookie cookie = suit.getSecureCookie(token);

        // then
        Assertions.assertThat(cookie).usingRecursiveComparison()
                .isEqualTo(goodCookie);
    }
}