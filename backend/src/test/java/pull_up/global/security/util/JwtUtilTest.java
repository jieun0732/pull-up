package pull_up.global.security.util;

import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import pull_up.domain.auth.dto.JwtUserInfoDto;
import pull_up.domain.auth.dto.OAuth2Login;
import pull_up.domain.auth.exception.AuthError;
import pull_up.domain.auth.exception.AuthException;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.*;

class JwtUtilTest {

    JwtUtil suit;

    @BeforeEach
    void init() {
        suit = new JwtUtil();
        ReflectionTestUtils.setField(suit, "key", "bvTAyAcnI3j1NPxTfJh9KLhBLQrrKdoS");
        ReflectionTestUtils.setField(suit, "subject", "test subject");
        ReflectionTestUtils.setField(suit, "issuer", "test issuer");
        ReflectionTestUtils.setField(suit, "expire", 259200000L);
    }

    @Test
    @DisplayName("Access Token 정상적으로 가져오는지 테스트")
    void testGetAccessToken() {
        // given
        OAuth2Login.Response dto = new OAuth2Login.Response(false, 1L, "kakao", "leaf", "test@example.com");

        // when
        String accessToken = suit.getAccessToken(dto);
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
        OAuth2Login.Response dto = new OAuth2Login.Response(false, 1L, "kakao", "leaf", "test@example.com");

        // when
        String accessToken = suit.getAccessToken(dto);
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
        exception.isInstanceOf(AuthException.class).hasMessage(AuthError.PARSE_JWT_ERROR.getMessage());
        exception2.isInstanceOf(AuthException.class).hasMessage(AuthError.PARSE_JWT_ERROR.getMessage());
        exception3.isInstanceOf(AuthException.class).hasMessage(AuthError.PARSE_JWT_ERROR.getMessage());
    }

    @Test
    @DisplayName("토큰으로부터 아이디, 이름, 이메일 가져오는지 테스트")
    void testGetUserInfo() {
        // given(2109년에 만료되는 토큰)
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwic3ViIjoidGVzdCBzdWJqZWN0IiwiaXNzIjoidGVzdCBpc3N1ZXIiLCJpYXQiOjE3MzA4NzI3MzEsImV4cCI6NDMyMjg3MjczMSwibmFtZSI6ImxlYWYiLCJlbWFpbCI6InRlc3RAZXhhbXBsZS5jb20iLCJyb2xlIjoiYXBwbGUtdXNlciJ9.ETNw7-7casHnF8tlN3w_7XZk5Wja4EpK7YRuVaOOBX4";

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
        // given(2109년에 만료되는 토큰)
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwic3ViIjoidGVzdCBzdWJqZWN0IiwiaXNzIjoidGVzdCBpc3N1ZXIiLCJpYXQiOjE3MzA4NzIzMTgsImV4cCI6NDMyMjg3MjMxOCwibmFtZSI6ImxlYWYiLCJlbWFpbCI6InRlc3RAZXhhbXBsZS5jb20iLCJyb2xlIjoiYXBwbGUtdXNlciJ9.9LIfVKam8uRgrrIhKkCfmOSPYYanjlQc_eSCYaMK72s";

        // when
        Authentication authentication = suit.getAuthentication(accessToken);

        // then
        assertThat(authentication).isInstanceOf(UsernamePasswordAuthenticationToken.class);
        assertThat(authentication.getCredentials()).isEqualTo(accessToken);
    }
}