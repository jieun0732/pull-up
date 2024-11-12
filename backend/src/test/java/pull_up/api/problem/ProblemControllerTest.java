package pull_up.api.problem;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nimbusds.jose.util.StandardCharset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pull_up.domain.problem.ProblemService;
import pull_up.global.dto.MessageDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProblemControllerTest {

    @Mock
    ProblemService mockProblemService;

    ProblemController suit;

    MockMvc mockMvc;

    Gson gson;

    @BeforeEach
    void init() {
        suit = new ProblemController(mockProblemService);
        mockMvc = MockMvcBuilders.standaloneSetup(suit).build();
        gson = new Gson();
    }

    @Test
    @DisplayName("전체 문제 삭제 요청 테스트")
    void testDeleteProblemHard() throws Exception {
        mockMvc.perform(delete("/api/pull-up/problems/hard"))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().json(gson.toJson(new MessageDto("All problem deleted successfully."))));
    }
}