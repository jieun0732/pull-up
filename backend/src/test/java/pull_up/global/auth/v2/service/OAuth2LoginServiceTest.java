package pull_up.global.auth.v2.service;

import com.google.gson.Gson;
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
import pull_up.global.auth.v2.dto.AppleJwks;
import pull_up.global.auth.v2.dto.AppleLoginRequestDto;
import pull_up.global.auth.v2.dto.OAuth2LoginResponseDto;
import pull_up.global.auth.v2.util.AppleTokenDecoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

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
    AppleAuthRestApi appleApi;

    @BeforeEach
    void init() {
        suit = new OAuth2LoginService(memberRepository, new AppleTokenDecoder(appleApi));
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
        AppleJwks jwk = gson.fromJson(appleKeyJson, AppleJwks.class);
        BDDMockito.when(appleApi.getKeys()).thenReturn(jwk);

        String idToken = "eyJraWQiOiJUOHRJSjF6U3JPIiwiYWxnIjoiUlMyNTYifQ.eyJpc3MiOiJodHRwczovL2FwcGxlaWQuYXBwbGUuY29tIiwiYXVkIjoiY29tLnB1bGwtdXAuc2VydmljZXMiLCJleHAiOjE3MzA0Mzk1NzIsImlhdCI6MTczMDM1MzE3Miwic3ViIjoiMDAxODc1LjhjMzEyNmMzMDE1NDQ3Yzc4NDEzNjY5MjlmYjgzMzU0LjAxMTEiLCJjX2hhc2giOiJubGUtam5ubVJnMmN6aVgtWFdJUTZRIiwiZW1haWwiOiJzcGVhcm9hZDE1QGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJhdXRoX3RpbWUiOjE3MzAzNTMxNzIsIm5vbmNlX3N1cHBvcnRlZCI6dHJ1ZX0.poH2ZPkg3LV41LxPMfggVkQTLMfQb9oC-ZLWi6KKn8T5FoOPPCTzJCEV7BKWJKhJ3J62IT9JQWoVkIJZpZTHekfXZV6iyEo24eLZeNDCXW3JWuQ62IvLaXaqcpTqWxBs0DUrB0BYaBIXakhEYdAnq7UoavwVBcoDz66M-waf3LBMu7q96RLyEkm3I-K0MK_tikKKpj9WZt2yq03BjLmlwwePN0qFPUNL_LCGFtNzAJn9ma5xXopg9GRd8V7J22LnuSxkLrxI3NXZ83AK-VG0HOqCL8p7NBD2xEd8fonFFhGP4lpdCKK40W1ueFc7E6WcDOzelruTYy9yMV6uJr00BA";
        AppleLoginRequestDto dto = new AppleLoginRequestDto(new AppleLoginRequestDto.UserName("상엽", "남"), "spearoad15@gmail.com");
        String userJson = gson.toJson(dto);

        // when
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

    String appleKeyJson = """
            {
              "keys": [
                {
                  "kty": "RSA",
                  "kid": "rBRfVmqsjn",
                  "use": "sig",
                  "alg": "RS256",
                  "n": "pPOaiF5yL-y42FaKg9PYASR5-rdTK7NEiteNUAzNp0zkta-HW-tgLNLNlsft3zcrsgOLqXxhX7qzlI3JGH-wSs7_v2XNSg57QhOTxPDqtUfy5DegtiSOgwE947OBTwCWo2R6cGZD1T8ysfO2HuKheq2hEwZU4Y-8qT19WWOhZHs4CVt7A5mzpIgWuUVw766VTyqrqKev32DOUPIqFocFz3tuty95S9t_OYnaPCcET-b6DV_eT7psPhqhl5nNUm0lzkCQ53-9kxQNJxBciy0wiBcAexD4KppKRRD3evFpOSxD1R6Kg2DIG5UnbVVqn5nhZA9RH-t50f_biqV3KlSHJQ",
                  "e": "AQAB"
                },
                {
                  "kty": "RSA",
                  "kid": "T8tIJ1zSrO",
                  "use": "sig",
                  "alg": "RS256",
                  "n": "teUbLrwScsjVrcFAvSrfben3eQaEca3ESBegGh_wdGuLKw6QgwDxY3fC1_WeSVnkJXx72ddw3j2inoADnTyzuNa_PwDSmvJhOhmzOmoltmtKHteGdaXrqMohO6A85WxVKbN7pzDqwZJNrdY12LOltlI8PHIG-elAbKM2XOHiJaZnLpAVckKy6MQYsEExpPB3plGxWZElqwNZY6SUDVeN-o9qg5FJOFg7T7iTVVEagws4DM6uZNMDQGtqg9V9VqPQkUzC-sYd5eqbB9LqH4iN5F6OB7BmD3g3jCu9zgh3O9V24N43EruBCNrmP0xLP5ZliKqozoAcd1nv71HuVm6mgQ",
                  "e": "AQAB"
                },
                {
                  "kty": "RSA",
                  "kid": "FftONTxoEg",
                  "use": "sig",
                  "alg": "RS256",
                  "n": "wio-SFzFvKKQ9vl5ctaYSi09o8k3Uh7r6Ht2eJv-hSaZ6A6xTXVIBVSm0KvPxaJlpjYPTCcl2sdEyXlD2Uh1khUKU7r9ON3rpN8pFHAere5ig_JGVEShxmt5E_jzMymYnSfkoSW44ulevQeUwP_MiC5VC1KJjTfD73ghX0tQ0-_RjTJJ2cLyFC4VFNboBMCVioUrz8IA3c0KIOl507qswQvMsh2vBTMDDSJfippAGLzUiWXxUlid-vyOC8GCtag61taSorxCw14irk-tsh7hWjDDkSTFn2gChPMfXXj10_lCv0UG29TVUVCAsay4pszzgmc4zwhgSsqQRd939BJexw",
                  "e": "AQAB"
                },
                {
                  "kty": "RSA",
                  "kid": "pggnQeNCOU",
                  "use": "sig",
                  "alg": "RS256",
                  "n": "xyWY7ydqTVHRzft5fPZmTuD9Ahk7-_2_IekZGy07Ovhj5IhYyVU8Hq5j0_c9m9tSdJTRdKmNjMURpY4ZJ_9rd3EOQ_WnYHM2cZIQ5y3f_WxeElnv_f2fKDruA-ERaQ6duov-3NAXC3oTWdXuRGRLbbfOVCahTjvnAA8YBRUe3llW7ZvTG14g-fAEQVlMYDxxCsbjtBJiUzKxbH-8KvhIhP9AJtiLDfiK1yzVJ7Qn6HNm5AUsFQKOAgTqxDMJkhi7pyntTyxhpkLYTEndaPRXth_LM3hVmaoFb3P3TsPCbDjSEbKy1wAndfPSzUk6qjyyBYhdXH0sgVpKMBAdggylLQ",
                  "e": "AQAB"
                }
              ]
            }
            """;
}