package pull_up.api.view;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pull_up.domain.problem.ProblemService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AdminProblemControllerTest {

    ProblemService mockService;
    MockMvc mockMvc;
    Gson gson;

    @BeforeEach
    void init() {
        mockService = Mockito.mock(ProblemService.class);
        AdminProblemController controller = new AdminProblemController(mockService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build();
        gson = new Gson();
    }

    @Test
    @DisplayName("전체 api 테스트")
    void testAllAPI() throws Exception {
        when(mockService.getAll(any())).thenReturn(Page.empty());
        mockMvc.perform(get("/admin/lists"))
                .andExpect(status().is(200));
        mockMvc.perform(get("/admin/lists/1"))
                .andExpect(status().is(200));
        mockMvc.perform(get("/admin/lists/new"))
                .andExpect(status().is(200));
        mockMvc.perform(post("/admin/lists"))
                .andExpect(status().is(200));
        mockMvc.perform(patch("/admin/lists/1"))
                .andExpect(status().is(200));
        mockMvc.perform(delete("/admin/lists/1"))
                .andExpect(status().is(302));
    }
}