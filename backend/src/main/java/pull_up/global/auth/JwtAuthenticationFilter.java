package pull_up.global.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pull_up.global.auth.v2.exception.OAuthException;
import pull_up.global.auth.v2.util.CookieUtil;
import pull_up.global.auth.v2.util.JwtUtil;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CookieUtil cookieUtil;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            setAuthentication(request);
            filterChain.doFilter(request, response);
        } catch (OAuthException e) {
            log.error("error while parsing cookie : {}", e.getMessage());
            filterChain.doFilter(request, response);
        }
    }

    private void setAuthentication(HttpServletRequest request) {
        String accessToken = cookieUtil.getAccessToken(request);
        Authentication authentication = jwtUtil.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
