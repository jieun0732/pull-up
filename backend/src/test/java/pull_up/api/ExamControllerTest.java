package pull_up.api;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pull_up.api.rest.ExamController;
import pull_up.domain.exam.ExamService;
import pull_up.domain.exam.ExamType;
import pull_up.domain.exam.dto.Grade;
import pull_up.domain.exam.dto.Start;
import pull_up.domain.exam.dto.Submit;
import pull_up.domain.problem.Entry;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ExamControllerTest {

    MockMvc mockMvc;

    Gson gson;

    @BeforeEach
    void init() {
        ExamService mockService = Mockito.mock(ExamService.class);
        ExamController controller = new ExamController(mockService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        gson = new Gson();
    }

    @Test
    @DisplayName("전체 api 테스트")
    void testAllAPI() throws Exception {

        Start.EvenlyRequest startEvenlyRequest = new Start.EvenlyRequest(1L, Entry.MATH);
        mockMvc.perform(post("/api/exams/evenly/start").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(startEvenlyRequest)))
                .andExpect(status().is(200));

        Start.ByProblemTypeRequest startByProblemTypeRequest = new Start.ByProblemTypeRequest(1L, Entry.MATH, "test");
        mockMvc.perform(post("/api/exams/by-problem-type/start").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(startByProblemTypeRequest)))
                .andExpect(status().is(200));

        Start.MockExamRequest startMockExamRequest = new Start.MockExamRequest(1L, ExamType.MOCK_EXAM.name());
        mockMvc.perform(post("/api/exams/mock-exam/start").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(startMockExamRequest)))
                .andExpect(status().is(200));

        Submit.Request submitRequest = new Submit.Request(1L, 1, 1);
        mockMvc.perform(post("/api/exams/submit").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(submitRequest)))
                .andExpect(status().is(200));

        mockMvc.perform(get("/api/exams/continue/1"))
                .andExpect(status().is(200));

        mockMvc.perform(delete("/api/exams/reset/1"))
                .andExpect(status().is(200));

        Grade.Request gradeRequest = new Grade.Request(1L, List.of());
        mockMvc.perform(post("/api/exams/mock-exam/grade").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(gradeRequest)))
                .andExpect(status().is(200));

        mockMvc.perform(get("/api/exams/next/1").param("problemNumber", "2"))
                .andExpect(status().is(200));

        mockMvc.perform(get("/api/exams/mock-exam/result/1"))
                .andExpect(status().is(200));

        mockMvc.perform(get("/api/exams/mock-exam/report/1"))
                .andExpect(status().is(200));

        mockMvc.perform(patch("/api/exams/end/1"))
                .andExpect(status().is(200));
    }
}