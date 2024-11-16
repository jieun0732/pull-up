package pull_up.global.security.v2.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.RedirectView;
import pull_up.api.auth.OAuth2Controller;
import pull_up.infra.database.entity.legacy.MemberL;
import pull_up.api.auth.dto.OAuth2LoginResponseDto;
import pull_up.domain.auth.SNSProvider;
import pull_up.domain.auth.service.OAuth2LoginService;
import pull_up.global.security.util.CookieUtil;
import pull_up.global.security.util.JwtUtil;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OAuth2ControllerTest {

    MockMvc mockMvc;
    OAuth2Controller suit;
    OAuth2LoginService service;

    @BeforeEach
    void init() {
        service = Mockito.mock(OAuth2LoginService.class);
        JwtUtil jwtUtil = new JwtUtil();
        CookieUtil cookieUtil = new CookieUtil();
        suit = new OAuth2Controller(service, jwtUtil, cookieUtil);
        mockMvc = MockMvcBuilders.standaloneSetup(suit).build();
        ReflectionTestUtils.setField(suit,"appleRedirectUri", "https//example.com");
        ReflectionTestUtils.setField(suit,"kakaoRedirectUri", "https//example.com");
        ReflectionTestUtils.setField(jwtUtil,"key", "bvTAyAcnI3j1NPxTfJh9KLhBLQrrKdoS");
        ReflectionTestUtils.setField(jwtUtil,"subject", "test subject");
        ReflectionTestUtils.setField(jwtUtil,"issuer", "test issuer");
        ReflectionTestUtils.setField(jwtUtil,"expire", 259200000L);
        ReflectionTestUtils.setField(cookieUtil, "domain", "https://example.com");
        ReflectionTestUtils.setField(cookieUtil, "maxAge", 1234);
        ReflectionTestUtils.setField(cookieUtil, "path", "/");
    }

    @Test
    @DisplayName("애플 로그인 시 회원가입 여부 추가")
    void testFirstLoginUser() {
        // given
        OAuth2LoginResponseDto user = OAuth2LoginResponseDto.builder()
                .firstLogin(true)
                .email("test@example.com")
                .name("leaf")
                .memberId(1L)
                .provider("apple")
                .build();

        // when
        RedirectView redirectView = suit.setRedirect(user);

        // then
        assertThat(redirectView.getAttributesMap()).hasFieldOrProperty("firstLogin")
                .extractingByKey("firstLogin").isEqualTo("true");
    }

    @Test
    @DisplayName("로그인 완료 후 토큰 추가")
    void testAddRequestToken() throws Exception {
        // given
        URI url = new URI("/api/pull-up/oauth2/callback/apple");
        URI url2 = new URI("/api/pull-up/oauth2/callback/kakao");
        MemberL memberL = MemberL.of("남상엽", "test@example.com", false, "apple-user");
        memberL.setId(1L);
        OAuth2LoginResponseDto appleUser = OAuth2LoginResponseDto.of(memberL, true, SNSProvider.APPLE);

        // when
        when(service.getAppleUser(any(), any())).thenReturn(appleUser);
        ResultActions result = mockMvc.perform(post(url).param("id_token", "test"));

        // when 2
        memberL.setRole("kakao-user");
        OAuth2LoginResponseDto kakaoUser = OAuth2LoginResponseDto.of(memberL, true, SNSProvider.APPLE);
        when(service.getKakaoUser((String) any())).thenReturn(kakaoUser);
        ResultActions result2 = mockMvc.perform(get(url2).param("code", "test"));

        // then
        result.andExpect(cookie().exists("accessToken"))
                .andExpect(cookie().httpOnly("accessToken", true))
                .andExpect(status().is3xxRedirection());

        result2.andExpect(cookie().exists("accessToken"))
                .andExpect(cookie().httpOnly("accessToken", true))
                .andExpect(status().is3xxRedirection());
    }
}