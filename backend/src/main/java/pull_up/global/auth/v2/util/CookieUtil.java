package pull_up.global.auth.v2.util;


import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
}
