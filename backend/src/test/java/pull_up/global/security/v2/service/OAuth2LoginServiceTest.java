package pull_up.global.security.v2.service;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
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
import pull_up.api.auth.dto.AppleLoginRequestDto;
import pull_up.api.auth.dto.KakaoUserInfoDto;
import pull_up.api.auth.dto.OAuth2LoginResponseDto;
import pull_up.domain.auth.service.OAuth2LoginService;
import pull_up.domain.member.MemberRepository;
import pull_up.global.security.util.AppleTokenDecoder;
import pull_up.infra.external_api.auth.KakaoAuthRestApi;

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

    private static void assertUser(OAuth2LoginResponseDto appleUser, String email, String provider, String name) {
        assertThat(appleUser.email()).isEqualTo(email);
        assertThat(appleUser.provider()).isEqualTo(provider);
        assertThat(appleUser.name()).isEqualTo(name);
    }

    @BeforeEach
    void init() {
        suit = new OAuth2LoginService(memberRepository, appleTokenDecoder, kakaoApi);
    }

    @Test
    @DisplayName("code로 로그인 테스트")
    void testKakaoCodeLogin() {
        // given
        KakaoUserInfoDto kakaoUserInfoDto = new KakaoUserInfoDto("123",
                new KakaoUserInfoDto.KakaoAccount("test@examle.com", true, true, true,
                        new KakaoUserInfoDto.KakaoAccount.Profile("leaf", true)));

        // when
        BDDMockito.when(kakaoApi.getUserInfo(any())).thenReturn(kakaoUserInfoDto);
        OAuth2LoginResponseDto kakaoUser = suit.getKakaoUser("test code");

        // then : 처음 로그인 시도하면 첫번째 로그인 true
        assertUser(kakaoUser, "test@examle.com", "kakao", "leaf");
        assertThat(kakaoUser.firstLogin()).isEqualTo(true);

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
        assertUser(kakaoUser, "test@example.com", "kakao", "남상엽");

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
        Claims sub = Jwts.claims().add("sub", "user sub").build();

        String idToken = "test token";
        AppleLoginRequestDto dto = new AppleLoginRequestDto(new AppleLoginRequestDto.UserName("상엽", "남"), "spearoad15@gmail.com");
        String userJson = gson.toJson(dto);

        // when
        BDDMockito.when(appleTokenDecoder.decode(any())).thenReturn(sub);
        OAuth2LoginResponseDto appleUser = suit.getAppleUser(idToken, userJson);

        // then : 처음 로그인 시도하면 첫번째 로그인 true
        assertUser(appleUser, "spearoad15@gmail.com", "apple", "남상엽");
        assertThat(appleUser.firstLogin()).isEqualTo(true);

        // when2 : 다시 로그인 시도
        OAuth2LoginResponseDto appleUser2 = suit.getAppleUser(idToken, "ALREADY_REGISTERED_USER");

        // then2 : 다시 로그인하면 첫번째 로그인 false
        assertThat(appleUser2.firstLogin()).isEqualTo(false);
    }

    @Test
    @DisplayName("이전에 로그인 했던 회원은 기존 아이디 그대로 사용")
    void usePreviousIdTest() {
        // given
        Gson gson = new Gson();
        Claims sub = Jwts.claims().add("sub", "user sub").build();

        String idToken = "test token";
        AppleLoginRequestDto dto = new AppleLoginRequestDto(new AppleLoginRequestDto.UserName("상엽", "남"), "spearoad15@gmail.com");
        String userJson = gson.toJson(dto);

        // when
        BDDMockito.when(appleTokenDecoder.decode(any())).thenReturn(sub);
        OAuth2LoginResponseDto appleUser = suit.getAppleUser(idToken, userJson); // 회원가입
        OAuth2LoginResponseDto appleUser2 = suit.getAppleUser(idToken, userJson); // 다시 회원가입

        // then
        assertUser(appleUser, "spearoad15@gmail.com", "apple", "남상엽");
        assertThat(appleUser).usingRecursiveComparison().ignoringFields("firstLogin").isEqualTo(appleUser2);
    }
}