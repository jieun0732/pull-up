package pull_up.api.member.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pull_up.api.member.dto.MemberScoreDto;
import pull_up.domain.member.MemberService;
import pull_up.infra.database.entity.legacy.MemberL;
import pull_up.infra.database.repository.member.MemberRepository;
import pull_up.domain.auth.SNSProvider;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.ANY,
        connection = EmbeddedDatabaseConnection.H2)
class MemberLServiceTest {

    @Autowired
    MemberRepository memberRepository;

    MemberService suit;

    MemberL memberL;

    @BeforeEach
    void init() {
        suit = new MemberService(memberRepository);
        memberL = MemberL.of("test", "test@privaterelay.appleid.com", false, SNSProvider.APPLE.getRole());
        memberRepository.save(memberL);
    }

    @Test
    @DisplayName("사용자 정보 조회 시 가려진 이메일 테스트")
    void testPrivateEmail() {
        // given

        // when
        MemberScoreDto dto = suit.getMemberById(memberL.getId());

        // then
        assertThat(dto.email()).isEqualTo("CONCEALED_EMAIL");
    }
}