package pull_up.api.auth;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import pull_up.global.exception.auth.AuthError;
import pull_up.global.exception.auth.AuthException;
import pull_up.global.security.util.CookieUtil;
import pull_up.global.security.util.JwtUtil;
import pull_up.api.auth.dto.OAuth2LoginResponseDto;
import pull_up.domain.auth.service.OAuth2LoginService;

import java.util.Map;
import java.util.Properties;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/oauth2/callback")
public class OAuth2Controller {

    @Value("${auth.apple.frontend-redirect-uri}")
    private String appleRedirectUri;

    @Value("${auth.kakao.frontend-redirect-uri}")
    private String kakaoRedirectUri;


    private final OAuth2LoginService oAuth2LoginService;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    @Operation(summary = "애플 SNS 로그인", description = "애플 SNS 로그인을 시도합니다.", tags = "인증")
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

    @Operation(summary = "카카오 SNS 로그인", description = "카카오 SNS 로그인을 시도합니다.", tags = "인증")
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
            throw new AuthException(AuthError.NOT_PROVIDED_OAUTH2_VENDOR_REQUEST);

        redirectView.setAttributes(attributes);
        return redirectView;
    }
}
