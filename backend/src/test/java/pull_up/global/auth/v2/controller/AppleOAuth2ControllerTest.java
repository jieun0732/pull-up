package pull_up.global.auth.v2.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.RedirectView;
import pull_up.api.member.entity.Member;
import pull_up.global.auth.v2.dto.OAuth2LoginResponseDto;
import pull_up.global.auth.v2.enums.OAuth2Provider;
import pull_up.global.auth.v2.service.OAuth2LoginService;
import pull_up.global.auth.v2.util.JwtUtil;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AppleOAuth2ControllerTest {

    MockMvc mockMvc;
    AppleOAuth2Controller suit;
    OAuth2LoginService service;

    @BeforeEach
    void init() {
        service = Mockito.mock(OAuth2LoginService.class);
        JwtUtil jwtUtil = new JwtUtil();
        suit = new AppleOAuth2Controller(service, jwtUtil);
        mockMvc = MockMvcBuilders.standaloneSetup(suit).build();
        ReflectionTestUtils.setField(suit,"redirectUri", "https//example.com");
        ReflectionTestUtils.setField(jwtUtil,"key", "bvTAyAcnI3j1NPxTfJh9KLhBLQrrKdoS");
        ReflectionTestUtils.setField(jwtUtil,"subject", "test subject");
        ReflectionTestUtils.setField(jwtUtil,"issuer", "test issuer");
        ReflectionTestUtils.setField(jwtUtil,"expire", 259200000L);
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
    @DisplayName("애플 로그인 완료 후 토큰 추가")
    void testAddRequestToken() throws Exception {
        // given
        URI url = new URI("/api/pull-up/oauth2/callback/apple");
        Member member = Member.of("남상엽", "test@example.com", false, "apple-user");
        member.setId(1L);
        OAuth2LoginResponseDto appleUser = OAuth2LoginResponseDto.of(member, true, OAuth2Provider.APPLE);

        // when
        when(service.getAppleUser(any(), any())).thenReturn(appleUser);
        ResultActions result = mockMvc.perform(post(url).param("id_token", "test"));

        // then
        result.andExpect(cookie().exists("accessToken"))
                .andExpect(cookie().httpOnly("accessToken", true))
                .andExpect(status().is3xxRedirection());
    }
}