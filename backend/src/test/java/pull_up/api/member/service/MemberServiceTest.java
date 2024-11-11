package pull_up.api.member.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pull_up.api.member.dto.MemberScoreDto;
import pull_up.api.member.entity.Member;
import pull_up.api.member.repository.MemberRepository;
import pull_up.global.auth.v2.enums.OAuth2Provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.ANY,
        connection = EmbeddedDatabaseConnection.H2)
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;

    MemberService suit;

    Member member;

    @BeforeEach
    void init() {
        suit = new MemberService(memberRepository);
        member = Member.of("test", "test@privaterelay.appleid.com", false, OAuth2Provider.APPLE.getRole());
        memberRepository.save(member);
    }

    @Test
    @DisplayName("사용자 정보 조회 시 가려진 이메일 테스트")
    void testPrivateEmail() {
        // given

        // when
        MemberScoreDto dto = suit.getMemberById(member.getId());

        // then
        assertThat(dto.email()).isEqualTo("CONCEALED_EMAIL");
    }
}