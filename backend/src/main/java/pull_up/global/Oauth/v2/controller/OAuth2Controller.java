package pull_up.global.Oauth.v2.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pull_up.global.Oauth.v2.api.KakaoAuthRestApi;
import pull_up.global.Oauth.v2.dto.AppleLoginRequestDto;
import pull_up.global.Oauth.v2.dto.KakaoLoginRequestDto;
import pull_up.global.Oauth.v2.dto.KakaoUserInfoDto;
import pull_up.global.Oauth.v2.dto.OAuth2LoginResponseDto;
import pull_up.global.Oauth.v2.service.OAuth2LoginService;
import pull_up.global.Oauth.v2.util.AppleTokenDecoder;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth2")
public class OAuth2Controller {

    private final KakaoAuthRestApi kakaoAuthRestApi;
    private final AppleTokenDecoder appleTokenDecoder;
    private final OAuth2LoginService userServiceV3;

    @Operation(summary = "애플 SNS 로그인", description = "애플에서 발급받은 token을 검증 후, 이메일과 이름을 반환합니다. 이 때, 처음 로그인 여부를 반드시 반환해야 합니다.", tags = "OAuth2")
    @PostMapping("/login/apple")
    ResponseEntity<OAuth2LoginResponseDto> appleLogin(@RequestBody AppleLoginRequestDto request) {
        Jws<Claims> decodedToken = appleTokenDecoder.decode(request.token());
        log.info("User Info By Token: {}", decodedToken);
        return new ResponseEntity<>(userServiceV3.getAppleUser(decodedToken, request), HttpStatus.OK);
    }

    @Operation(summary = "카카오 SNS 로그인", description = "카카오에서 발급받은 code를 검증 후, 이메일과 이름을 반환합니다.", tags = "OAuth2")
    @PostMapping("/login/kakao")
    ResponseEntity<OAuth2LoginResponseDto> kakaoLogin(@RequestBody KakaoLoginRequestDto request) {
        KakaoUserInfoDto userInfo = kakaoAuthRestApi.getUserInfo(request);
        log.info("User Info By token: {}", userInfo);
        return new ResponseEntity<>(userServiceV3.getKakaoUser(userInfo), HttpStatus.OK);
    }
}
