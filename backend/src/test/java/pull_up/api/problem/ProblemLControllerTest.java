package pull_up.api.problem;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pull_up.api.problem.dto.CreateProblem;
import pull_up.api.problem.dto.ProblemDto;
import pull_up.domain.problem.Entry;
import pull_up.domain.problem.ProblemService;
import pull_up.global.dto.ListDto;
import pull_up.global.dto.MessageDto;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProblemLControllerTest {

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
        CreateProblem.Request request = new CreateProblem.Request(Entry.MATH, "", "", "", "", "", "", "", "", "", "");
        String body = gson.toJson(request);
        MessageDto expect = new MessageDto("Problem 1 has been created successfully.");

        // when
        when(mockService.createProblem((CreateProblem.Request) any())).thenReturn(expect);

        // then
        mockMvc.perform(post("/api/problems").contentType(APPLICATION_JSON).content(body))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().json(gson.toJson(expect)));
    }

    @Test
    @DisplayName("스프레드시트 문제 생성 요청 테스트")
    void testCreateProblemWithSheet() throws Exception {
        // given
        MessageDto expect = new MessageDto("10 Problems have been created successfully.");

        // when
        when(mockService.createProblem((String) any())).thenReturn(expect);

        // then
        mockMvc.perform(post("/api/problems/format").contentType(APPLICATION_JSON).content("\"test\""))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().json(gson.toJson(expect)));
    }

}