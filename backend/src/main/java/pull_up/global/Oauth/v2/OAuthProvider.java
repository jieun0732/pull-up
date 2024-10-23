package pull_up.global.Oauth.v2;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuthProvider {
  APPLE("apple"), KAKAO("kakao");

  private final String value;
}
