package pull_up.global.auth.v2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import pull_up.global.auth.v2.exception.OAuthException;

import static pull_up.global.auth.v2.enums.OAuth2Provider.KAKAO;
import static pull_up.global.auth.v2.exception.OAuthError.NOT_PROVIDED_OAUTH2_VENDOR_REQUEST;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2UserServiceV2 extends DefaultOAuth2UserService {

  private final OAuth2LoginService oAuth2LoginService;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    String provider = userRequest.getClientRegistration().getRegistrationId();

    if (provider.equalsIgnoreCase(KAKAO.getValue())) {
      return loadKakaoUser(userRequest);
    }

    throw new OAuthException(NOT_PROVIDED_OAUTH2_VENDOR_REQUEST);
  }

  private OAuth2User loadKakaoUser(OAuth2UserRequest userRequest) {
//    log.info("user parameters : {} ", userRequest.getAdditionalParameters());
//    String userJson = (String) userRequest.getAdditionalParameters().get("kakao_account");
//    log.info("user json : {}", userJson);
//    log.info("email : {}", userRequest.getAdditionalParameters().get("email"));
//    log.info("name : {}", userRequest.getAdditionalParameters().get("nickName"));
//    OAuth2LoginResponseDto kakaoUser = oAuth2LoginService.getKakaoUser(userJson);
//    userRequest.getAdditionalParameters().put("memberId", kakaoUser.memberId());
    return super.loadUser(userRequest);
  }
}