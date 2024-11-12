package pull_up.global.security.v2.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.test.util.ReflectionTestUtils;
import pull_up.global.security.handler.OAuth2SuccessHandler;
import pull_up.infra.database.entity.Member;
import pull_up.api.auth.dto.OAuth2LoginResponseDto;
import pull_up.domain.auth.enums.OAuth2Provider;
import pull_up.domain.auth.service.OAuth2LoginService;
import pull_up.global.security.util.CookieUtil;
import pull_up.global.security.util.JwtUtil;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class OAuth2SuccessHandlerTest {

    OAuth2SuccessHandler suit;
    OAuth2LoginService service;

    @BeforeEach
    void init() {

        service = Mockito.mock(OAuth2LoginService.class);
        JwtUtil jwtUtil = new JwtUtil();
        CookieUtil cookieUtil = new CookieUtil();
        suit = new OAuth2SuccessHandler(service, jwtUtil, cookieUtil);
        ReflectionTestUtils.setField(suit,"redirectUri", "https//example.com");
        ReflectionTestUtils.setField(jwtUtil, "key", "bvTAyAcnI3j1NPxTfJh9KLhBLQrrKdoS");
        ReflectionTestUtils.setField(jwtUtil, "subject", "test subject");
        ReflectionTestUtils.setField(jwtUtil, "issuer", "test issuer");
        ReflectionTestUtils.setField(jwtUtil, "expire", 259200000L);
        ReflectionTestUtils.setField(cookieUtil, "domain", "https://example.com");
        ReflectionTestUtils.setField(cookieUtil, "maxAge", 1234);
        ReflectionTestUtils.setField(cookieUtil, "path", "/");
    }

    @Test
    @DisplayName("로그인 완료 후 토큰 추가")
    void testAddRequestToken() throws Exception {
        // given
        URI url = new URI("/api/pull-up/oauth2/callback/kakao");

        // create authentication
        Map<String, Object> attributes = new HashMap<>();
        Map<String, Object> kakaoAccount = new HashMap<>();
        Map<String, Object> profile = new HashMap<>();
        profile.put("nickname", "남상엽");
        kakaoAccount.put("profile", profile);
        kakaoAccount.put("email", "test@example.com");
        attributes.put("kakao_account", kakaoAccount);
        attributes.put("id", 123);
        DefaultOAuth2User user = new DefaultOAuth2User(new ArrayList<>(), attributes, "id");

        // mock kakao user
        Member member = Member.of("남상엽", "test@example.com", false, "kakao-user");
        member.setId(1L);
        OAuth2LoginResponseDto kakaoUser = OAuth2LoginResponseDto.of(member, true, OAuth2Provider.KAKAO);

        // mock filter param
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        Authentication authentication = new TestingAuthenticationToken(user, null, "Role_kakao-user");

        // when
        when(service.getKakaoUser((DefaultOAuth2User) any())).thenReturn(kakaoUser);
        suit.onAuthenticationSuccess(request, response, authentication);

        // then
        assertThat(response.getHeaderNames()).contains(HttpHeaders.SET_COOKIE);
        assertThat(response.getHeader(HttpHeaders.SET_COOKIE)).contains("accessToken");
    }
}