package pull_up.domain.auth.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuth2Provider {
    APPLE("apple", "apple_user"), KAKAO("kakao", "kakao_user");
    private final String value;
    private final String role;
}
