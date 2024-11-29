package pull_up.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import pull_up.domain.auth.dto.OAuth2Login;
import pull_up.domain.auth.exception.AuthError;
import pull_up.domain.auth.exception.AuthException;
import pull_up.domain.auth.service.OAuth2LoginService;
import pull_up.global.security.util.CookieUtil;
import pull_up.global.security.util.JwtUtil;

import java.util.Map;
import java.util.Properties;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/oauth2/callback")
public class OAuth2Controller {

    private final OAuth2LoginService oAuth2LoginService;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    @Value("${auth.apple.frontend-redirect-uri}")
    private String appleRedirectUri;

    @Value("${auth.kakao.frontend-redirect-uri}")
    private String kakaoRedirectUri;

    @Operation(summary = "애플 SNS 로그인", description = "애플 SNS 로그인을 시도합니다.", tags = "인증")
    @PostMapping("/apple")
    RedirectView appleLogin(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> parameterMap = request.getParameterMap();

        OAuth2Login.Response appleUser = oAuth2LoginService.getAppleUser(
                parameterMap.get("id_token")[0],
                parameterMap.getOrDefault("user", new String[]{"ALREADY_REGISTERED_USER"})[0]
        );

        response.addCookie(cookieUtil.getSecureCookie(jwtUtil.getAccessToken(appleUser)));
        return setRedirect(appleUser);
    }

    @Operation(summary = "카카오 SNS 로그인", description = "카카오 SNS 로그인을 시도합니다.", tags = "인증")
    @GetMapping("/kakao")
    RedirectView kakaoLogin(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> parameterMap = request.getParameterMap();

        OAuth2Login.Response kakaoUser = oAuth2LoginService.getKakaoUser(parameterMap.get("code")[0]);

        response.addCookie(cookieUtil.getSecureCookie(jwtUtil.getAccessToken(kakaoUser)));
        return setRedirect(kakaoUser);
    }

    @Operation(summary = "로컬 SNS 기본계정 로그인[테스트 전용]", description = "로컬 SNS로 로그인합니다.", tags = "인증")
    @PostMapping("/local")
    ResponseEntity<Properties> localLoginDefault(HttpServletResponse response) {
        OAuth2Login.Response localUser = oAuth2LoginService.getLocalUser();
        response.addCookie(cookieUtil.getSecureCookie(jwtUtil.getAccessToken(localUser)));
        return new ResponseEntity<>(getUserDtoProperty(localUser), HttpStatus.OK);
    }

    @Operation(summary = "로컬 SNS 로그인[테스트 전용]", description = "로컬 SNS로 로그인합니다.", tags = "인증")
    @GetMapping("/local/login/{snsId}")
    ResponseEntity<Properties> localLogin(@PathVariable String snsId, HttpServletResponse response) {
        OAuth2Login.Response localUser = oAuth2LoginService.getLocalUser(snsId);
        response.addCookie(cookieUtil.getSecureCookie(jwtUtil.getAccessToken(localUser)));
        return new ResponseEntity<>(getUserDtoProperty(localUser), HttpStatus.OK);
    }


    @Operation(summary = "로컬 SNS 회원가입[테스트 전용]", description = "로컬 SNS로 회원가입합니다.", tags = "인증")
    @PostMapping("/local/regist")
    ResponseEntity<Properties> localRegist(@RequestBody OAuth2Login.Request.Local request, HttpServletResponse response) {
        OAuth2Login.Response localUser = oAuth2LoginService.getLocalUser(request);
        response.addCookie(cookieUtil.getSecureCookie(jwtUtil.getAccessToken(localUser)));
        return new ResponseEntity<>(getUserDtoProperty(localUser), HttpStatus.OK);
    }

    public RedirectView setRedirect(OAuth2Login.Response userDto) {
        Properties attributes = getUserDtoProperty(userDto);
        RedirectView redirectView = new RedirectView();

        if (userDto.provider().equalsIgnoreCase("apple"))
            redirectView.setUrl(appleRedirectUri);
        else if (userDto.provider().equalsIgnoreCase("kakao"))
            redirectView.setUrl(kakaoRedirectUri);
        else throw new AuthException(AuthError.NOT_PROVIDED_OAUTH2_VENDOR_REQUEST);

        redirectView.setAttributes(attributes);

        return redirectView;
    }

    private Properties getUserDtoProperty(OAuth2Login.Response userDto) {
        Properties attributes = new Properties();
        attributes.setProperty("firstLogin", userDto.firstLogin().toString());
        attributes.setProperty("memberId", userDto.memberId().toString());
        attributes.setProperty("name", userDto.name());
        attributes.setProperty("email", userDto.email());
        attributes.setProperty("provider", userDto.provider());
        return attributes;
    }
}
