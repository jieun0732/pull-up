package pull_up.global.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import pull_up.api.auth.dto.OAuth2LoginResponseDto;
import pull_up.domain.auth.service.OAuth2LoginService;
import pull_up.global.security.util.CookieUtil;
import pull_up.global.security.util.JwtUtil;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final OAuth2LoginService oAuth2LoginService;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    @Value("${auth.kakao.frontend-redirect-uri}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
        OAuth2LoginResponseDto kakaoUser = oAuth2LoginService.getKakaoUser(user);

        String redirectUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("firstLogin", URLEncoder.encode(kakaoUser.firstLogin().toString(), StandardCharsets.UTF_8))
                .queryParam("memberId", URLEncoder.encode(kakaoUser.memberId().toString(), StandardCharsets.UTF_8))
                .queryParam("name", URLEncoder.encode(kakaoUser.name(), StandardCharsets.UTF_8))
                .queryParam("email", URLEncoder.encode(kakaoUser.email(), StandardCharsets.UTF_8))
                .queryParam("provider", URLEncoder.encode(kakaoUser.provider(), StandardCharsets.UTF_8))
                .build().toUriString();

        response.addCookie(cookieUtil.getSecureCookie(jwtUtil.getAccessToken(kakaoUser)));
        response.sendRedirect(redirectUrl);
    }
}