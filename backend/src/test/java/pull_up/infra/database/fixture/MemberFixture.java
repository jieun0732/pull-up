package pull_up.infra.database.fixture;

import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import pull_up.domain.auth.enums.OAuth2Provider;
import pull_up.infra.database.entity.Member;

@RequiredArgsConstructor
public enum MemberFixture {
    APPLE_USER("apple test user", "test@example.com", OAuth2Provider.APPLE.getRole()),
    KAKAO_USER("kakao test user", "test@example.com", OAuth2Provider.KAKAO.getRole());

    private final String name;
    private final String email;
    private final String role;

    public Member get() {
        return Member.of(name, email, false,role);
    }
}
