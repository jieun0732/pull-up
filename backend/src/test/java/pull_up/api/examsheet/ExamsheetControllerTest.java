package pull_up.api.examsheet;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pull_up.api.exam.ExamController;
import pull_up.domain.exam.ExamService;
import pull_up.domain.exam.dto.Start;
import pull_up.domain.examsheet.ExamsheetService;
import pull_up.domain.examsheet.dto.CreateExamsheet;
import pull_up.domain.problem.Entry;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ExamsheetControllerTest {

    MockMvc mockMvc;

    Gson gson;

    @BeforeEach
    void init() {
        ExamsheetService mockService = Mockito.mock(ExamsheetService.class);
        ExamsheetController controller = new ExamsheetController(mockService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        gson = new Gson();
    }
    
    @Test
    @DisplayName("전체 api 테스트")
    void testAllAPI() throws Exception {
        CreateExamsheet.Request request = new CreateExamsheet.Request("모의고사", null);
        mockMvc.perform(post("/api/examsheets").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(request)))
                .andExpect(status().is(200));
    }
}