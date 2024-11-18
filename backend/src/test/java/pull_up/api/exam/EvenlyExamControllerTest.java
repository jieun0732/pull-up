package pull_up.api.exam;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pull_up.domain.exam.EvenlyExamService;
import pull_up.domain.exam.ExamType;
import pull_up.domain.exam.dto.Start;
import pull_up.domain.exam.dto.Submit;
import pull_up.domain.problem.Entry;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EvenlyExamControllerTest {

    MockMvc mockMvc;

    Gson gson;

    @BeforeEach
    void init() {
        EvenlyExamService evenlyExamService = Mockito.mock(EvenlyExamService.class);
        EvenlyExamController controller = new EvenlyExamController(evenlyExamService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        gson = new Gson();
    }

    @Test
    @DisplayName("전체 요청 테스트")
    void testAllRequest() throws Exception {
        Start.Request startRequest = new Start.Request(1L, ExamType.EVENLY, Entry.MATH);
        mockMvc.perform(post("/api/exams/evenly/start").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(startRequest)))
                .andExpect(status().is(200));

        Submit.Request submitRequest = new Submit.Request(1L, 1, 1);
        mockMvc.perform(post("/api/exams/evenly/submit").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(submitRequest)))
                .andExpect(status().is(200));

        mockMvc.perform(get("/api/exams/evenly/next/1").param("problemNumber", "1"))
                .andExpect(status().is(200));

        mockMvc.perform(patch("/api/exams/evenly/end/1"))
                .andExpect(status().is(200));
    }

}