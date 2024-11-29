package pull_up.api;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pull_up.api.controller.MemberController;
import pull_up.domain.member.MemberService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest {

    MockMvc mockMvc;

    Gson gson;

    @BeforeEach
    void init() {
        MemberService mockService = Mockito.mock(MemberService.class);
        MemberController controller = new MemberController(mockService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        gson = new Gson();
    }


    @Test
    @DisplayName("전체 api 테스트")
    void testAllAPI() throws Exception {
        mockMvc.perform(get("/api/members/entry-exam/MATH/solved").param("memberId", "1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
        mockMvc.perform(get("/api/members/mock-exam/solved").param("memberId", "1"))
                .andDo(print()).andExpect(status().is(200));
        mockMvc.perform(get("/api/members/1"))
                .andExpect(status().is(200));
        mockMvc.perform(patch("/api/members/tutorial/1"))
                .andExpect(status().is(200));
        mockMvc.perform(get("/api/members/incorrect/1"))
                .andExpect(status().is(200));
    }
}