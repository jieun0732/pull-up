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
import pull_up.domain.exam.ExamType;
import pull_up.domain.exam.dto.Start;
import pull_up.domain.problem.Entry;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ByProblemTypeExamLControllerTest {

    MockMvc mockMvc;

    Gson gson;

    @BeforeEach
    void init() {
        ExamService examService = Mockito.mock(ExamService.class);
        ByProblemTypeExamController controller = new ByProblemTypeExamController(examService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        gson = new Gson();
    }

    @Test
    @DisplayName("전체 요청 테스트")
    void testAllRequest() throws Exception {
        Start.ByProblemTypeRequest startByProblemTypeRequest = new Start.ByProblemTypeRequest(1L, ExamType.EVENLY, Entry.MATH, "test");
        mockMvc.perform(post("/api/exams/by-problem-type/start").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(startByProblemTypeRequest)))
                .andExpect(status().is(200));
    }
}