package pull_up.global.auth.v2.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import pull_up.global.auth.v2.util.JwtUtil;
import pull_up.global.auth.v2.dto.OAuth2LoginResponseDto;
import pull_up.global.auth.v2.service.OAuth2LoginService;

import java.util.Map;
import java.util.Properties;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AppleOAuth2Controller {

    @Value("${auth.apple.frontend-redirect-uri}")
    private String redirectUri;

    private final OAuth2LoginService oAuth2LoginService;
    private final JwtUtil jwtUtil;

    @PostMapping("/api/pull-up/oauth2/callback/apple")
    RedirectView appleLogin(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> parameterMap = request.getParameterMap();

        OAuth2LoginResponseDto appleUser = oAuth2LoginService.getAppleUser(
                parameterMap.get("id_token")[0],
                parameterMap.getOrDefault("user", new String[] {"ALREADY_REGISTERED_USER"})[0]
        );

        response.addCookie(new Cookie("accessToken", jwtUtil.getAccessToken(appleUser)));

        return setRedirect(appleUser);
    }

    public RedirectView setRedirect(OAuth2LoginResponseDto appleUser) {
        Properties attributes = new Properties();
        attributes.setProperty("firstLogin", appleUser.firstLogin().toString());
        attributes.setProperty("memberId", appleUser.memberId().toString());
        attributes.setProperty("name", appleUser.name());
        attributes.setProperty("email", appleUser.email());
        attributes.setProperty("provider", appleUser.provider());

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(redirectUri);
        redirectView.setAttributes(attributes);
        return redirectView;
    }
}
