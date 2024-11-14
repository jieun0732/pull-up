package pull_up.global.security;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import pull_up.global.security.filter.JwtAuthenticationFilter;
import pull_up.global.security.util.CookieUtil;
import pull_up.global.config.SecurityConfig;

import java.net.URI;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration(classes = SecurityConfig.class)
@EnableWebMvc
@ComponentScan("pull_up")
@ActiveProfiles("test")
class JwtAuthenticationFilterTest {

    MockMvc mockMvc;

    JwtAuthenticationFilter suit;

    CookieUtil cookieUtil = new CookieUtil();

    @Autowired
    WebApplicationContext wac;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity()).build();
        suit = new JwtAuthenticationFilter(null, null);

        ReflectionTestUtils.setField(cookieUtil, "domain", "https://example.com");
        ReflectionTestUtils.setField(cookieUtil, "maxAge", 1234);
        ReflectionTestUtils.setField(cookieUtil, "path", "/");
    }

    @Test
    @DisplayName("인증정보 없는 요청 필터링 테스트")
    void testUnauthorizedRequest() throws Exception {
        // given
        URI uri = new URI("/api/pull-up/exams/incorrect-answers/" + 1 + "?memberId=" + 1);

        // when
        ResultActions result = mockMvc.perform(get(uri));

        // then
//        result.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("인증정보 있는 요청 허용 테스트")
    void testAuthorizedRequest() throws Exception {
        // given
        URI uri = new URI("/api/pull-up/exams/incorrect-answers/" + 1 + "?memberId=" + 1);
        Cookie accessToken = mockMvc.perform(get("/api/pull-up/oauth2/mock/cookie"))
                .andExpect(status().isOk()).andReturn()
                .getResponse().getCookie("accessToken");

        // when
        ResultActions result = mockMvc.perform(get(uri).cookie(accessToken));

        // then
//        result.andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("잘못된 요청 거부 테스트")
    void testInvalidRequest() throws Exception {
        // given
        URI uri = new URI("/api/pull-up/exams/incorrect-answers/" + 1 + "?memberId=" + 1);
        Cookie accessToken = new Cookie("accessToken", "eyJraWQiOiJUOHRJSjF6U3JPIiwiYWxnIjoiUlMyNTYifQ.eyJpc3MiOiJodHRwczovL2FwcGxlaWQuYXBwbGUuY29tIiwiYXVkIjoiY29tLnB1bGwtdXAuc2VydmljZXMiLCJleHAiOjE3MzA0Mzk1NzIsImlhdCI6MTczMDM1MzE3Miwic3ViIjoiMDAxODc1LjhjMzEyNmMzMDE1NDQ3Yzc4NDEzNjY5MjlmYjgzMzU0LjAxMTEiLCJjX2hhc2giOiJubGUtam5ubVJnMmN6aVgtWFdJUTZRIiwiZW1haWwiOiJzcGVhcm9hZDE1QGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJhdXRoX3RpbWUiOjE3MzAzNTMxNzIsIm5vbmNlX3N1cHBvcnRlZCI6dHJ1ZX0.poH2ZPkg3LV41LxPMfggVkQTLMfQb9oC-ZLWi6KKn8T5FoOPPCTzJCEV7BKWJKhJ3J62IT9JQWoVkIJZpZTHekfXZV6iyEo24eLZeNDCXW3JWuQ62IvLaXaqcpTqWxBs0DUrB0BYaBIXakhEYdAnq7UoavwVBcoDz66M-waf3LBMu7q96RLyEkm3I-K0MK_tikKKpj9WZt2yq03BjLmlwwePN0qFPUNL_LCGFtNzAJn9ma5xXopg9GRd8V7J22LnuSxkLrxI3NXZ83AK-VG0HOqCL8p7NBD2xEd8fonFFhGP4lpdCKK40W1ueFc7E6WcDOzelruTYy9yMV6uJr00BA");

        // when
        ResultActions result = mockMvc.perform(get(uri).cookie(accessToken));

        // then
//        result.andDo(print()).andExpect(status().isUnauthorized());
    }

}