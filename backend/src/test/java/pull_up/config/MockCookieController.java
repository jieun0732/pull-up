package pull_up.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pull_up.global.auth.v2.util.CookieUtil;
import pull_up.global.auth.v2.util.JwtUtil;

@RestController
@RequiredArgsConstructor
public class MockCookieController {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    @GetMapping("/api/pull-up/oauth2/mock/cookie")
    String getCookie(HttpServletResponse response) {
        String accessToken = jwtUtil.getAccessToken(1234L, "test user", "test@example.com", "test-user");
        Cookie secureCookie = cookieUtil.getSecureCookie(accessToken);
        response.addCookie(secureCookie);
        System.out.println("secureCookie = " + secureCookie.getValue());

        return "ok";
    }
}
