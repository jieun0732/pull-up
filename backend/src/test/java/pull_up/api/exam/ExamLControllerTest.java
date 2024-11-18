package pull_up.api.exam;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pull_up.api.deprecated.ExamController;
import pull_up.api.exam.dto.ExamGrade;
import pull_up.domain.deprecated.ExamServiceL;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ExamLControllerTest {

    ExamController suit;

    ExamServiceL examServiceL;

    MockMvc mockMvc;

    Gson gson;

    @BeforeEach
    void init() {
        examServiceL = Mockito.mock(ExamServiceL.class);
        suit = new ExamController(examServiceL);
        mockMvc = MockMvcBuilders.standaloneSetup(suit).build();
        gson = new Gson();
    }

    @Test
    @DisplayName("POST /api/exams/mock-exam/grade")
    void testGradeApi() throws Exception {
        ExamGrade.Request request = new ExamGrade.Request(1L, List.of(new ExamGrade.SelectedAnswer(1L, 1)));
        mockMvc.perform(post("/api/exams/mock-exam/grade")
                        .content(gson.toJson(request))
                        .contentType(APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().is(200));
    }

    @Test
    @DisplayName("GET /api/exams/incorrect-answers")
    void testIncorrectAnswer() throws Exception {
        mockMvc.perform(get("/api/exams/incorrect-answers?memberId=1")
                        .contentType(APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().is(200));
        mockMvc.perform(get("/api/exams/incorrect-answers/1")
                        .contentType(APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().is(200));

    }
}