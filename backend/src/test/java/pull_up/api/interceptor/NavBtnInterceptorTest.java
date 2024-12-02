package pull_up.api.interceptor;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class NavBtnInterceptorTest {

    @Test
    @DisplayName("Domain 파싱 테스트")
    void testParseDomain() {
        // given
        NavBtnInterceptor suit = new NavBtnInterceptor();
        MockHttpServletRequest adminReq = new MockHttpServletRequest();
        adminReq.setRequestURI("/admin");
        MockHttpServletRequest problemReq = new MockHttpServletRequest();
        problemReq.setRequestURI("/admin/problems");
        MockHttpServletRequest examsheetReq = new MockHttpServletRequest();
        examsheetReq.setRequestURI("/admin/examsheets");

        // when
        String admin = suit.getDomain(adminReq);
        String problems = suit.getDomain(problemReq);
        String examsheets = suit.getDomain(examsheetReq);

        // then
        assertThat(admin).isEqualTo("");
        assertThat(problems).isEqualTo("problems");
        assertThat(examsheets).isEqualTo("examsheets");
    }
}