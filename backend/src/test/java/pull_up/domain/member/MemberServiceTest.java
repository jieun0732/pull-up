package pull_up.domain.member;

import org.junit.jupiter.api.BeforeEach;
import pull_up.infra.database.jpa.entity.Member;
import pull_up.infra.database.jpa.fixture.MemberFixture;

class MemberServiceTest {
    MemberService suit;
    Member member;

    @BeforeEach
    void init() {
        suit = new MemberService(null);
        member = MemberFixture.APPLE_USER.get();
    }

}
