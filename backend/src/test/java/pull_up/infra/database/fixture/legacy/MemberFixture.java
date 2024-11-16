package pull_up.infra.database.fixture.legacy;

import lombok.RequiredArgsConstructor;
import pull_up.domain.auth.SNSProvider;
import pull_up.infra.database.entity.legacy.MemberL;

@RequiredArgsConstructor
public enum MemberFixture {
    APPLE_USER("apple test user", "test@example.com", SNSProvider.APPLE.getRole()),
    KAKAO_USER("kakao test user", "test@example.com", SNSProvider.KAKAO.getRole());

    private final String name;
    private final String email;
    private final String role;

    public MemberL get() {
        return MemberL.of(name, email, false,role);
    }
}
