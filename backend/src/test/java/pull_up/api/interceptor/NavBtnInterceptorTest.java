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
        MockHttpServletRequest problemReq = new MockHttpServletRequest();
        problemReq.setRequestURI("/admin/problems");
        MockHttpServletRequest examsheetReq = new MockHttpServletRequest();
        examsheetReq.setRequestURI("/admin/examsheets");

        // when
        String problems = suit.getDomain(problemReq);
        String examsheets = suit.getDomain(examsheetReq);

        // then
        assertThat(problems).isEqualTo("problems");
        assertThat(examsheets).isEqualTo("examsheets");
    }
}