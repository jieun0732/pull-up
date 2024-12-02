package pull_up.domain.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SNSProvider {
    APPLE, KAKAO, LOCAL, NONE;
}
