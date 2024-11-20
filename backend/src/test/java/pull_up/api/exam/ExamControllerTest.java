package pull_up.api.exam;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pull_up.domain.exam.ExamService;
import pull_up.domain.exam.dto.Submit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ExamControllerTest {

    MockMvc mockMvc;

    Gson gson;

    @BeforeEach
    void init() {
        ExamService examService = Mockito.mock(ExamService.class);
        ExamController controller = new ExamController(examService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        gson = new Gson();
    }

    @Test
    @DisplayName("전체 요청 테스트")
    void testAllRequest() throws Exception {
        mockMvc.perform(get("/api/exams/solved").param("memberId", "1").param("entry", "MATH").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));

        Submit.Request submitRequest = new Submit.Request(1L, 1, 1);
        mockMvc.perform(post("/api/exams/submit").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(submitRequest)))
                .andExpect(status().is(200));

        mockMvc.perform(get("/api/exams/next/1").param("problemNumber", "1"))
                .andExpect(status().is(200));

        mockMvc.perform(patch("/api/exams/end/1"))
                .andExpect(status().is(200));
    }

}