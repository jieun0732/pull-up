package pull_up.global.Oauth.v2.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuthProvider {
    APPLE("apple", "apple_user"), KAKAO("kakao", "kakao_user");
    private final String value;
    private final String role;
}
