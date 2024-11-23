package pull_up.api.problem;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pull_up.domain.exam.dto.Grade;
import pull_up.domain.exam.dto.Start;
import pull_up.domain.exam.dto.Submit;
import pull_up.domain.problem.Entry;
import pull_up.domain.problem.ProblemService;
import pull_up.domain.problem.dto.CreateProblem;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProblemControllerTest {

    MockMvc mockMvc;

    Gson gson;

    @BeforeEach
    void init() {
        ProblemService mockService = Mockito.mock(ProblemService.class);
        ProblemController controller = new ProblemController(mockService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        gson = new Gson();
    }

    @Test
    @DisplayName("전체 api 테스트")
    void testAllAPI() throws Exception {
        CreateProblem.Request request = new CreateProblem.Request(Entry.MATH, "problemType", "question", "explanation", "choice1", "choice2", "choice3", "choice4", "choice5", "answer", "answerExplanation");
        mockMvc.perform(post("/api/problems").content(gson.toJson(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));

        mockMvc.perform(post("/api/problems/format").content("test").contentType(MediaType.TEXT_PLAIN))
                .andDo(print())
                .andExpect(status().is(200));
    }
}