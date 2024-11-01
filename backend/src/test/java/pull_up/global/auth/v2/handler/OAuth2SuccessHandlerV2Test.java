package pull_up.global.auth.v2.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pull_up.api.member.entity.Member;
import pull_up.global.auth.v2.dto.OAuth2LoginResponseDto;
import pull_up.global.auth.v2.enums.OAuth2Provider;
import pull_up.global.auth.v2.service.OAuth2LoginService;
import pull_up.global.auth.v2.util.CookieUtil;
import pull_up.global.auth.v2.util.JwtUtil;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.framework;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.testSecurityContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OAuth2SuccessHandlerV2Test {

    OAuth2SuccessHandlerV2 suit;
    OAuth2LoginService service;

    @BeforeEach
    void init() {

        service = Mockito.mock(OAuth2LoginService.class);
        JwtUtil jwtUtil = new JwtUtil();
        CookieUtil cookieUtil = new CookieUtil();
        suit = new OAuth2SuccessHandlerV2(service, jwtUtil, cookieUtil);
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