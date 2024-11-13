package pull_up.api.problem;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pull_up.api.problem.dto.CreateProblem;
import pull_up.domain.problem.ProblemService;
import pull_up.global.dto.MessageDto;
import pull_up.infra.database.entity.Problem;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProblemControllerTest {

    @Mock
    ProblemService mockService;

    ProblemController suit;

    MockMvc mockMvc;

    Gson gson;

    @BeforeEach
    void init() {
        suit = new ProblemController(mockService);
        mockMvc = MockMvcBuilders.standaloneSetup(suit).build();
        gson = new Gson();
    }

    @Test
    @DisplayName("문제 생성 요청 테스트")
    void testCreateProblem() throws Exception {
        // given
        CreateProblem.Request request = new CreateProblem.Request("", "", "", "", "", "", "", "", "", "", "", "", 0D);
        String body = gson.toJson(request);

        // when
        BDDMockito.when(mockService.createProblem((CreateProblem.Request) any())).thenReturn(new MessageDto("Problem 1 has been created successfully."));

        // then
        mockMvc.perform(post("/api/pull-up/problems").contentType(APPLICATION_JSON).content(body))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().json(gson.toJson(new MessageDto("Problem 1 has been created successfully."))));
    }

    @Test
    @DisplayName("스프레드시트 문제 생성 요청 테스트")
    void testCreateProblemWithSheet() throws Exception {
        // given

        // when
        BDDMockito.when(mockService.createProblem((String) any())).thenReturn(new MessageDto("10 Problems have been created successfully."));

        // then
        mockMvc.perform(post("/api/pull-up/problems/format").contentType(APPLICATION_JSON).content("\"test\""))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().json(gson.toJson(new MessageDto("10 Problems have been created successfully."))));
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