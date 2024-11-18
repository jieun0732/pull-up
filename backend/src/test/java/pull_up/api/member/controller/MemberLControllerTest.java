package pull_up.api.member.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pull_up.api.deprecated.MemberController;
import pull_up.domain.deprecated.MemberServiceL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MemberLControllerTest {

    MockMvc mockMvc;

    MemberController suit;

    @Mock
    MemberServiceL memberServiceL;

    @BeforeEach
    void init() {
        suit = new MemberController(memberServiceL);
        mockMvc = MockMvcBuilders.standaloneSetup(suit).build();

    }

    @Test
    @DisplayName("DELETE /api/members/{id}/delete/hard")
    void testDeleteMember() throws Exception {
        mockMvc.perform(delete("/api/members/" + 1 + "/delete/hard"))
                .andDo(print())
                .andExpect(status().is(200));
    }
}