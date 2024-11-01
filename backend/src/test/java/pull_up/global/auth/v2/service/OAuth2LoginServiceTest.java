package pull_up.global.auth.v2.service;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import pull_up.api.member.repository.MemberRepository;
import pull_up.global.auth.v2.api.AppleAuthRestApi;
import pull_up.global.auth.v2.api.KakaoAuthRestApi;
import pull_up.global.auth.v2.dto.AppleJwks;
import pull_up.global.auth.v2.dto.AppleLoginRequestDto;
import pull_up.global.auth.v2.dto.KakaoUserInfoDto;
import pull_up.global.auth.v2.dto.OAuth2LoginResponseDto;
import pull_up.global.auth.v2.util.AppleTokenDecoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.ANY,
        connection = EmbeddedDatabaseConnection.H2)
@ExtendWith(MockitoExtension.class)
class OAuth2LoginServiceTest {

    OAuth2LoginService suit;

    @Autowired
    MemberRepository memberRepository;

    @Mock
    AppleTokenDecoder appleTokenDecoder;

    @Mock
    KakaoAuthRestApi kakaoApi;

    @BeforeEach
    void init() {
        suit = new OAuth2LoginService(memberRepository, appleTokenDecoder, kakaoApi);
    }

    @Test
    @DisplayName("code로 로그인 테스트")
    void testKakaoCodeLogin() {
        // given
        KakaoUserInfoDto kakaoUserInfoDto = new KakaoUserInfoDto(123L,
                new KakaoUserInfoDto.KakaoAccount("test@examle.com", true, true, true,
                        new KakaoUserInfoDto.KakaoAccount.Profile("leaf", true)));

        // when
        BDDMockito.when(kakaoApi.getUserInfo(any())).thenReturn(kakaoUserInfoDto);
        OAuth2LoginResponseDto kakaoUser = suit.getKakaoUser("test code");

        // then : 처음 로그인 시도하면 첫번째 로그인 true
        assertThat(kakaoUser.email()).isEqualTo("test@examle.com");
        assertThat(kakaoUser.firstLogin()).isEqualTo(true);
        assertThat(kakaoUser.provider()).isEqualTo("kakao");
        assertThat(kakaoUser.name()).isEqualTo("leaf");

        // when2 : 다시 로그인 시도
        OAuth2LoginResponseDto kakaoUser2 = suit.getKakaoUser("test code");

        // then2 : 다시 로그인하면 첫번째 로그인 false
        assertThat(kakaoUser2.firstLogin()).isEqualTo(false);
    }

    @Test
    @DisplayName("이미 등록된 카카오 회원 로그인 테스트")
    void testAlreadyRegisteredKakaoUser() {
        // given
        Map<String, Object> attributes = new HashMap<>();
        Map<String, Object> kakaoAccount = new HashMap<>();
        Map<String, Object> profile = new HashMap<>();
        profile.put("nickname", "남상엽");
        kakaoAccount.put("profile", profile);
        kakaoAccount.put("email", "test@example.com");
        attributes.put("kakao_account", kakaoAccount);
        attributes.put("id", 123);

        DefaultOAuth2User user = new DefaultOAuth2User(new ArrayList<>(), attributes, "id");

        // when
        OAuth2LoginResponseDto kakaoUser = suit.getKakaoUser(user);

        // then : 처음 로그인 시도하면 첫번째 로그인 true
        assertThat(kakaoUser.email()).isEqualTo("test@example.com");
        assertThat(kakaoUser.firstLogin()).isEqualTo(true);
        assertThat(kakaoUser.provider()).isEqualTo("kakao");
        assertThat(kakaoUser.name()).isEqualTo("남상엽");

        // when2 : 다시 로그인 시도
        OAuth2LoginResponseDto kakaoUser2 = suit.getKakaoUser(user);

        // then2 : 다시 로그인하면 첫번째 로그인 false
        assertThat(kakaoUser2.firstLogin()).isEqualTo(false);
    }

    @Test
    @DisplayName("이미 등록된 애플 회원 로그인 테스트")
    void testAlreadyRegisteredAppleUser() {
        // given
        Gson gson = new Gson();
        Claims email = Jwts.claims().add("email", "spearoad15@gmail.com").build();

        String idToken = "test token";
        AppleLoginRequestDto dto = new AppleLoginRequestDto(new AppleLoginRequestDto.UserName("상엽", "남"), "spearoad15@gmail.com");
        String userJson = gson.toJson(dto);

        // when
        BDDMockito.when(appleTokenDecoder.decode(any())).thenReturn(email);
        OAuth2LoginResponseDto appleUser = suit.getAppleUser(idToken, userJson);

        // then : 처음 로그인 시도하면 첫번째 로그인 true
        assertThat(appleUser.email()).isEqualTo("spearoad15@gmail.com");
        assertThat(appleUser.firstLogin()).isEqualTo(true);
        assertThat(appleUser.provider()).isEqualTo("apple");
        assertThat(appleUser.name()).isEqualTo("남상엽");

        // when2 : 다시 로그인 시도
        OAuth2LoginResponseDto appleUser2 = suit.getAppleUser(idToken, "ALREADY_REGISTERED_USER");

        // then2 : 다시 로그인하면 첫번째 로그인 false
        assertThat(appleUser2.firstLogin()).isEqualTo(false);
    }
}