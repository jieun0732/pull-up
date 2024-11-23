package pull_up.domain.auth;

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
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import pull_up.config.annotation.IntegrationTest;
import pull_up.domain.auth.dto.KakaoDto;
import pull_up.domain.auth.dto.OAuth2Login;
import pull_up.domain.auth.service.OAuth2LoginService;
import pull_up.domain.dao.MemberRepository;
import pull_up.global.security.util.AppleTokenDecoder;
import pull_up.infra.external_api.auth.KakaoAuthRestApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@IntegrationTest
@ExtendWith(MockitoExtension.class)
class OAuth2LoginServiceTest {

    OAuth2LoginService suit;

    @Autowired
    MemberRepository memberRepository;

    @Mock
    AppleTokenDecoder appleTokenDecoder;

    @Mock
    KakaoAuthRestApi kakaoApi;

    private static void assertUser(OAuth2Login.Response appleUser, String email, String provider, String name) {
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
        KakaoDto.KakaoUserInfo kakaoUserInfo = new KakaoDto.KakaoUserInfo("123",
                new KakaoDto.KakaoUserInfo.KakaoAccount("test@examle.com", true, true, true,
                        new KakaoDto.KakaoUserInfo.KakaoAccount.Profile("leaf", true)));

        // when
        when(kakaoApi.getUserInfo(any())).thenReturn(kakaoUserInfo);
        OAuth2Login.Response kakaoUser = suit.getKakaoUser("test code");

        // then : 처음 로그인 시도하면 첫번째 로그인 true
        assertUser(kakaoUser, "test@examle.com", "kakao", "leaf");
        assertThat(kakaoUser.firstLogin()).isEqualTo(true);

        // when2 : 다시 로그인 시도
        OAuth2Login.Response kakaoUser2 = suit.getKakaoUser("test code");

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
        OAuth2Login.Response kakaoUser = suit.getKakaoUser(user);

        // then : 처음 로그인 시도하면 첫번째 로그인 true
        assertUser(kakaoUser, "test@example.com", "kakao", "남상엽");

        // when2 : 다시 로그인 시도
        OAuth2Login.Response kakaoUser2 = suit.getKakaoUser(user);

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
        OAuth2Login.Request.Apple dto = new OAuth2Login.Request.Apple(new OAuth2Login.Request.Apple.UserName("상엽", "남"), "spearoad15@gmail.com");
        String userJson = gson.toJson(dto);

        // when
        when(appleTokenDecoder.decode(any())).thenReturn(sub);
        OAuth2Login.Response appleUser = suit.getAppleUser(idToken, userJson);

        // then : 처음 로그인 시도하면 첫번째 로그인 true
        assertUser(appleUser, "spearoad15@gmail.com", "apple", "남상엽");
        assertThat(appleUser.firstLogin()).isEqualTo(true);

        // when2 : 다시 로그인 시도
        OAuth2Login.Response appleUser2 = suit.getAppleUser(idToken, "ALREADY_REGISTERED_USER");

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
        OAuth2Login.Request.Apple dto = new OAuth2Login.Request.Apple(new OAuth2Login.Request.Apple.UserName("상엽", "남"), "spearoad15@gmail.com");
        String userJson = gson.toJson(dto);

        // when
        when(appleTokenDecoder.decode(any())).thenReturn(sub);
        OAuth2Login.Response appleUser = suit.getAppleUser(idToken, userJson); // 회원가입
        OAuth2Login.Response appleUser2 = suit.getAppleUser(idToken, userJson); // 다시 회원가입

        // then
        assertUser(appleUser, "spearoad15@gmail.com", "apple", "남상엽");
        assertThat(appleUser).usingRecursiveComparison().ignoringFields("firstLogin").isEqualTo(appleUser2);
    }
    
    @Test
    @DisplayName("임시 로그인 테스트")
    void testLocalLogin() {
        // given
    
        // when
        OAuth2Login.Response localUser = suit.getLocalUser();

        // then
        assertThat(localUser.email()).isEqualTo("test@example.com");
        assertThat(localUser.name()).isEqualTo("test user");
        assertThat(localUser.memberId()).isEqualTo(99999999);
        assertThat(localUser.provider()).isEqualTo("local");
        assertThat(localUser.firstLogin()).isTrue();
    }
}