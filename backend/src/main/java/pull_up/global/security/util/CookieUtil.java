package pull_up.global.security.util;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pull_up.global.exception.auth.AuthError;
import pull_up.global.exception.auth.AuthException;

import java.util.Arrays;

@Slf4j
@Component
public class CookieUtil {

    @Value("${auth.cookie.domain}")
    private String domain;

    @Value("${auth.cookie.path}")
    private String path;

    @Value("${auth.cookie.max-age}")
    private Integer maxAge;

    public Cookie getSecureCookie(String token) {
        Cookie cookie = new Cookie("accessToken", token);
        cookie.setDomain(domain);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setAttribute("SameSite", "None");

        return cookie;
    }

    public String getAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0)
            throw new AuthException(AuthError.NO_ACCESS_TOKEN_IN_COOKIES);

        return Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("accessToken"))
                .findFirst()
                .orElseThrow(() -> new AuthException(AuthError.NO_ACCESS_TOKEN_IN_COOKIES))
                .getValue();
    }
}
