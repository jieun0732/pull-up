package pull_up.global.auth.v2.util;

import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import pull_up.api.member.entity.Member;
import pull_up.global.auth.v2.dto.JwtUserInfoDto;
import pull_up.global.auth.v2.exception.OAuthError;
import pull_up.global.auth.v2.exception.OAuthException;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtUtilTest {

    JwtUtil suit;

    @BeforeEach
    void init() {
        suit = new JwtUtil();
        ReflectionTestUtils.setField(suit,"key", "bvTAyAcnI3j1NPxTfJh9KLhBLQrrKdoS");
        ReflectionTestUtils.setField(suit,"subject", "test subject");
        ReflectionTestUtils.setField(suit,"issuer", "test issuer");
        ReflectionTestUtils.setField(suit,"expire", 259200000L);
    }

    @Test
    @DisplayName("Access Token 정상적으로 가져오는지 테스트")
    void testGetAccessToken() {
        // given
        Member member = Member.of("leaf", "test@example.com", true, "apple-user");
        member.setId(1L);

        // when
        String accessToken = suit.getAccessToken(member);
        String tokenBody = accessToken.split("\\.")[1];

        // then
        assertThat(tokenBody).asBase64Decoded().contains("jti : 1".getBytes(StandardCharsets.US_ASCII));
        assertThat(tokenBody).asBase64Decoded().contains("name : leaf".getBytes(StandardCharsets.UTF_8));
        assertThat(tokenBody).asBase64Decoded().contains("email : test@example.com".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("Access Token 정상 검증하는지 테스트")
    void testValidateAccessToken() {
        // given
        Member member = Member.of("leaf", "test@example.com", true, "apple-user");
        member.setId(1L);

        // when
        String accessToken = suit.getAccessToken(member);
        System.out.println("accessToken = " + accessToken);

        // then
        suit.validate(accessToken);
    }

    @Test
    @DisplayName("유효하지 않은 Access Token 테스트")
    void testInvalidateAccessToken() {
        // given
        // subject : invalid subject
        String invalidSubjectToken = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwic3ViIjoiaW52YWxpZCBzdWJqZWN0IiwiaXNzIjoidGVzdCBpc3N1ZXIiLCJpYXQiOjE3MzAzODc5NjAsImV4cCI6MTczMDY0NzE2MCwibmFtZSI6ImxlYWYiLCJlbWFpbCI6InRlc3RAZXhhbXBsZS5jb20ifQ.XfHyQJVb2AkS770PEP8DfwpEmfJoUgupYsTvo7XqH_k";

        // exp : 현재시간 이전
        String invalidExpirationToken = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwic3ViIjoidGVzdCBzdWJqZWN0IiwiaXNzIjoidGVzdCBpc3N1ZXIiLCJpYXQiOjE3MzAzODc5NjAsImV4cCI6MTczMDM4Nzk2MCwibmFtZSI6ImxlYWYiLCJlbWFpbCI6InRlc3RAZXhhbXBsZS5jb20ifQ.kAtsPACOi397cybeqARAgCSsFfE4V1nOhStcyyBNoFY";

        // secret key : 잘못된 키 사용
        String invalidSecretKey = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwic3ViIjoidGVzdCBzdWJqZWN0IiwiaXNzIjoidGVzdCBpc3N1ZXIiLCJpYXQiOjE3MzAzODgxODEsImV4cCI6MTczMDY0NzM4MSwibmFtZSI6ImxlYWYiLCJlbWFpbCI6InRlc3RAZXhhbXBsZS5jb20ifQ.rm7cd4HQb_lsFQKprS4fQ9wRFfKuf5OfHy2b3L4LtVc";

        // when
        AbstractThrowableAssert<?, ? extends Throwable> exception = assertThatThrownBy(() -> suit.validate(invalidSubjectToken));
        AbstractThrowableAssert<?, ? extends Throwable> exception2 = assertThatThrownBy(() -> suit.validate(invalidExpirationToken));
        AbstractThrowableAssert<?, ? extends Throwable> exception3 = assertThatThrownBy(() -> suit.validate(invalidSecretKey));

        // then
        exception.isInstanceOf(OAuthException.class).hasMessage(OAuthError.PARSE_JWT_ERROR.getMessage());
        exception2.isInstanceOf(OAuthException.class).hasMessage(OAuthError.PARSE_JWT_ERROR.getMessage());
        exception3.isInstanceOf(OAuthException.class).hasMessage(OAuthError.PARSE_JWT_ERROR.getMessage());
    }

    @Test
    @DisplayName("토큰으로부터 아이디, 이름, 이메일 가져오는지 테스트")
    void testGetUserInfo() {
        // given
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwic3ViIjoidGVzdCBzdWJqZWN0IiwiaXNzIjoidGVzdCBpc3N1ZXIiLCJpYXQiOjE3MzAzODg3NzUsImV4cCI6MTczMDY0Nzk3NSwibmFtZSI6ImxlYWYiLCJlbWFpbCI6InRlc3RAZXhhbXBsZS5jb20iLCJyb2xlIjoiYXBwbGUtdXNlciJ9.rkjDf4MJBfdptTQBlfnYtWMmgTP-2eBsncMIlOKKXyA";

        // when
        JwtUserInfoDto dto = suit.getUserInfo(accessToken);

        // then
        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.name()).isEqualTo("leaf");
        assertThat(dto.email()).isEqualTo("test@example.com");
        assertThat(dto.role()).isEqualTo("apple-user");
    }

    @Test
    @DisplayName("토큰으로부터 Authentication 객체 가져오는지 테스트")
    void testGetAuthentication() {
        // given
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwic3ViIjoidGVzdCBzdWJqZWN0IiwiaXNzIjoidGVzdCBpc3N1ZXIiLCJpYXQiOjE3MzAzODg3NzUsImV4cCI6MTczMDY0Nzk3NSwibmFtZSI6ImxlYWYiLCJlbWFpbCI6InRlc3RAZXhhbXBsZS5jb20iLCJyb2xlIjoiYXBwbGUtdXNlciJ9.rkjDf4MJBfdptTQBlfnYtWMmgTP-2eBsncMIlOKKXyA";

        // when
        Authentication authentication = suit.getAuthentication(accessToken);

        // then
        assertThat(authentication).isInstanceOf(UsernamePasswordAuthenticationToken.class);
        assertThat(authentication.getCredentials()).isEqualTo(accessToken);
    }
}