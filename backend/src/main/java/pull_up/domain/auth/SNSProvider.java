package pull_up.domain.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SNSProvider {
    APPLE("apple", "apple_user"), KAKAO("kakao", "kakao_user");
    private final String value;
    private final String role;
}
