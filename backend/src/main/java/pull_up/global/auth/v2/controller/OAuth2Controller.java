package pull_up.global.auth.v2.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import pull_up.global.auth.v2.exception.OAuthError;
import pull_up.global.auth.v2.exception.OAuthException;
import pull_up.global.auth.v2.util.CookieUtil;
import pull_up.global.auth.v2.util.JwtUtil;
import pull_up.global.auth.v2.dto.OAuth2LoginResponseDto;
import pull_up.global.auth.v2.service.OAuth2LoginService;

import java.util.Map;
import java.util.Properties;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/pull-up/oauth2/callback")
public class OAuth2Controller {

    @Value("${auth.apple.frontend-redirect-uri}")
    private String appleRedirectUri;

    @Value("${auth.kakao.frontend-redirect-uri}")
    private String kakaoRedirectUri;


    private final OAuth2LoginService oAuth2LoginService;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    @PostMapping("/apple")
    RedirectView appleLogin(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> parameterMap = request.getParameterMap();

        OAuth2LoginResponseDto appleUser = oAuth2LoginService.getAppleUser(
                parameterMap.get("id_token")[0],
                parameterMap.getOrDefault("user", new String[] {"ALREADY_REGISTERED_USER"})[0]
        );

        response.addCookie(cookieUtil.getSecureCookie(jwtUtil.getAccessToken(appleUser)));
        return setRedirect(appleUser);
    }

    @GetMapping("/kakao")
    RedirectView kakaoLogin(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> parameterMap = request.getParameterMap();

        OAuth2LoginResponseDto kakaoUser = oAuth2LoginService.getKakaoUser(parameterMap.get("code")[0]);

        response.addCookie(cookieUtil.getSecureCookie(jwtUtil.getAccessToken(kakaoUser)));
        return setRedirect(kakaoUser);
    }

    public RedirectView setRedirect(OAuth2LoginResponseDto userDto) {
        Properties attributes = new Properties();
        attributes.setProperty("firstLogin", userDto.firstLogin().toString());
        attributes.setProperty("memberId", userDto.memberId().toString());
        attributes.setProperty("name", userDto.name());
        attributes.setProperty("email", userDto.email());
        attributes.setProperty("provider", userDto.provider());

        RedirectView redirectView = new RedirectView();

        if (userDto.provider().equalsIgnoreCase("apple"))
            redirectView.setUrl(appleRedirectUri);
        else if (userDto.provider().equalsIgnoreCase("kakao"))
            redirectView.setUrl(kakaoRedirectUri);
        else
            throw new OAuthException(OAuthError.NOT_PROVIDED_OAUTH2_VENDOR_REQUEST);

        redirectView.setAttributes(attributes);
        return redirectView;
    }
}
