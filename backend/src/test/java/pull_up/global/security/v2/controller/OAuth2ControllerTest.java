package pull_up.global.security.v2.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.RedirectView;
import pull_up.api.auth.OAuth2Controller;
import pull_up.domain.auth.dto.OAuth2LoginResponseDto;
import pull_up.domain.auth.SNSProvider;
import pull_up.domain.auth.service.OAuth2LoginService;
import pull_up.global.security.util.CookieUtil;
import pull_up.global.security.util.JwtUtil;
import pull_up.infra.database.jpa.entity.Member;
import pull_up.infra.database.jpa.fixture.MemberFixture;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OAuth2ControllerTest {

    MockMvc mockMvc;
    OAuth2Controller suit;
    OAuth2LoginService mockService;

    @BeforeEach
    void init() {
        mockService = Mockito.mock(OAuth2LoginService.class);
        JwtUtil jwtUtil = new JwtUtil();
        CookieUtil cookieUtil = new CookieUtil();
        suit = new OAuth2Controller(mockService, jwtUtil, cookieUtil);
        mockMvc = MockMvcBuilders.standaloneSetup(suit).build();
        ReflectionTestUtils.setField(suit, "appleRedirectUri", "https//example.com");
        ReflectionTestUtils.setField(suit, "kakaoRedirectUri", "https//example.com");
        ReflectionTestUtils.setField(jwtUtil, "key", "bvTAyAcnI3j1NPxTfJh9KLhBLQrrKdoS");
        ReflectionTestUtils.setField(jwtUtil, "subject", "test subject");
        ReflectionTestUtils.setField(jwtUtil, "issuer", "test issuer");
        ReflectionTestUtils.setField(jwtUtil, "expire", 259200000L);
        ReflectionTestUtils.setField(cookieUtil, "domain", "https://example.com");
        ReflectionTestUtils.setField(cookieUtil, "maxAge", 1234);
        ReflectionTestUtils.setField(cookieUtil, "path", "/");
        ReflectionTestUtils.setField(cookieUtil, "withCredential", true);
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
        Member member = MemberFixture.APPLE_USER.get();
        member.setId(1L);
        OAuth2LoginResponseDto appleUser = OAuth2LoginResponseDto.of(member, true, SNSProvider.APPLE);

        // when
        when(mockService.getAppleUser(any(), any())).thenReturn(appleUser);
        ResultActions result = mockMvc.perform(post("/api/oauth2/callback/apple").param("id_token", "test"));

        // when 2
        Member member2 = MemberFixture.KAKAO_USER.get();
        member2.setId(1L);
        OAuth2LoginResponseDto kakaoUser = OAuth2LoginResponseDto.of(member2, true, SNSProvider.APPLE);
        when(mockService.getKakaoUser((String) any())).thenReturn(kakaoUser);
        ResultActions result2 = mockMvc.perform(get("/api/oauth2/callback/kakao").param("code", "test"));

        // then
        result.andExpect(cookie().exists("accessToken"))
                .andExpect(cookie().httpOnly("accessToken", true))
                .andExpect(status().is3xxRedirection());

        result2.andExpect(cookie().exists("accessToken"))
                .andExpect(cookie().httpOnly("accessToken", true))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("로컬 로그인 테스트")
    void testLocalLogin() throws Exception {
        when(mockService.getLocalUser()).thenReturn(new OAuth2LoginResponseDto(true,99999999L, "local", "test user", "test@example.com"));
        mockMvc.perform(post("/api/oauth2/callback/local")).andDo(print())
                .andExpect(status().is(200));
    }
}