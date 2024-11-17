package pull_up.infra.database.fixture;

import lombok.RequiredArgsConstructor;
import pull_up.domain.auth.Role;
import pull_up.domain.auth.SNSProvider;
import pull_up.infra.database.entity.Member;

@RequiredArgsConstructor
public enum MemberFixture implements Fixture<Member> {
    APPLE_USER(1L, false, "apple test user", "test@apple.com", "test1234", SNSProvider.APPLE, Role.USER),
    KAKAO_USER(2L, false, "apple test user", "test@kakao.com", "test1234", SNSProvider.APPLE, Role.USER),
    TUTORIAL_FINISHED_USER(3L, true, "tutorial finished user", "test@apple.com", "test1234", SNSProvider.APPLE, Role.USER),
    APPLE_EMAIL_CONCEALED_USER(4L, false, "conceal email user", "test@privaterelay.appleid.com", "test1234", SNSProvider.APPLE, Role.USER);

    private final Long id;
    private final Boolean tutorialFinished;
    private final String name;
    private final String email;
    private final String snsId;
    private final SNSProvider snsProvider;
    private final Role role;

    public Member get() {
        return new Member(id, tutorialFinished, name, email, snsId, snsProvider, role);
    }
}
