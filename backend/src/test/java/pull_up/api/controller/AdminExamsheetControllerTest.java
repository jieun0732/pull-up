package pull_up.api.controller;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pull_up.domain.examsheet.ExamsheetService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminExamsheetControllerTest {

    ExamsheetService mockService;
    MockMvc mockMvc;
    Gson gson;

    @BeforeEach
    void init() {
        mockService = Mockito.mock(ExamsheetService.class);
        AdminExamsheetController controller = new AdminExamsheetController(mockService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build();
        gson = new Gson();
    }

    @Test
    @DisplayName("전체 api 테스트")
    void testAllApi() throws Exception {
        mockMvc.perform(get("/admin/examsheets")).andDo(print()).andExpect(status().is(200));
    }
}