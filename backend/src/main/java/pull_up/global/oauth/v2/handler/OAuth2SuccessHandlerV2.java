package pull_up.global.oauth.v2.handler;

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
import pull_up.global.oauth.v2.dto.OAuth2LoginResponseDto;
import pull_up.global.oauth.v2.service.OAuth2LoginService;

import java.io.IOException;
import java.io.UTFDataFormatException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandlerV2 implements AuthenticationSuccessHandler {

    private final OAuth2LoginService oAuth2LoginService;
    @Value("${auth.login.redirect-uri}")
    private String URI;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
        OAuth2LoginResponseDto kakaoUser = oAuth2LoginService.getKakaoUser(user);

        String redirectUrl = UriComponentsBuilder.fromUriString(URI)
                .queryParam("login", URLEncoder.encode(kakaoUser.login().toString(), StandardCharsets.UTF_8))
                .queryParam("memberId", URLEncoder.encode(kakaoUser.memberId().toString(), StandardCharsets.UTF_8))
                .queryParam("name", URLEncoder.encode(kakaoUser.name(), StandardCharsets.UTF_8))
                .queryParam("email", URLEncoder.encode(kakaoUser.email(), StandardCharsets.UTF_8))
                .queryParam("provider", URLEncoder.encode(kakaoUser.provider(), StandardCharsets.UTF_8))
                .build().toUriString();

        response.sendRedirect(redirectUrl);
    }
}