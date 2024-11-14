package pull_up.api.exam;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pull_up.api.exam.dto.GradeExam;
import pull_up.domain.exam.ExamService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ExamControllerTest {

    ExamController suit;

    ExamService examService;

    MockMvc mockMvc;

    Gson gson;

    @BeforeEach
    void init() {
        examService = Mockito.mock(ExamService.class);
        suit = new ExamController(examService);
        mockMvc = MockMvcBuilders.standaloneSetup(suit).build();
        gson = new Gson();
    }

    @Test
    @DisplayName("문제 채점 요청 테스트")
    void testGradeApi() throws Exception {
        // given
        GradeExam.Request request = new GradeExam.Request(1L, List.of(new GradeExam.SelectedAnswer(1L, 1)));

        // when
        mockMvc.perform(post("/api/pull-up/exams/mock-exam/grade")
                        .content(gson.toJson(request))
                        .contentType(APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())

                // then
                .andExpect(status().is(200));
    }
}