package pull_up.infra.database.jpa.fixture;

import lombok.RequiredArgsConstructor;
import pull_up.domain.auth.Role;
import pull_up.domain.auth.SNSProvider;
import pull_up.infra.database.jpa.entity.Member;

@RequiredArgsConstructor
public enum MemberFixture implements Fixture<Member> {
    DELETED_USER(-1L, false, "DELETED_USER", "DELETED_USER", "DELETED_USER", null, SNSProvider.NONE, Role.NONE),
    APPLE_USER(1L, false, "apple test user", "test@apple.com", "test1234", "REFRESH_TOKEN", SNSProvider.APPLE, Role.USER),
    KAKAO_USER(2L, false, "kakao test user", "test@kakao.com", "test1234", null, SNSProvider.KAKAO, Role.USER),
    TUTORIAL_FINISHED_USER(3L, true, "tutorial finished user", "test@apple.com", "test1234", "REFRESH_TOKEN", SNSProvider.APPLE, Role.USER),
    APPLE_EMAIL_CONCEALED_USER(4L, false, "conceal email user", "test@privaterelay.appleid.com", "test1234", "REFRESH_TOKEN", SNSProvider.APPLE, Role.USER);

    private final Long id;
    private final Boolean tutorialFinished;
    private final String name;
    private final String email;
    private final String snsId;
    private final String refreshToken;
    private final SNSProvider snsProvider;
    private final Role role;

    public Member get() {
        return new Member(id, tutorialFinished, name, email, snsId, refreshToken, snsProvider, role);
    }
}
