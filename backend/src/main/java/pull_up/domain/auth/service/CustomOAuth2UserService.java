package pull_up.domain.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import pull_up.global.exception.auth.AuthException;

import static pull_up.domain.auth.enums.OAuth2Provider.KAKAO;
import static pull_up.global.exception.auth.AuthError.NOT_PROVIDED_OAUTH2_VENDOR_REQUEST;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final OAuth2LoginService oAuth2LoginService;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    String provider = userRequest.getClientRegistration().getRegistrationId();

    if (provider.equalsIgnoreCase(KAKAO.getValue())) {
      return loadKakaoUser(userRequest);
    }

    throw new AuthException(NOT_PROVIDED_OAUTH2_VENDOR_REQUEST);
  }

  private OAuth2User loadKakaoUser(OAuth2UserRequest userRequest) {
    return super.loadUser(userRequest);
  }
}