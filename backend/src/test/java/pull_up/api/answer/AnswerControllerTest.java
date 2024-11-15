package pull_up.api.answer;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pull_up.api.answer.dto.CreateAnswer;
import pull_up.domain.answer.AnswerService;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AnswerControllerTest {

    AnswerService mockService;
    AnswerController suit;
    MockMvc mockMvc;
    Gson gson;

    @BeforeEach
    void init() {
        mockService = Mockito.mock(AnswerService.class);
        suit = new AnswerController(mockService);
        mockMvc = MockMvcBuilders.standaloneSetup(suit).build();
        gson = new Gson();
    }

    @Test
    @DisplayName("Answer 생성 테스트")
    void testCreateAnswer() throws Exception {
        // given
        CreateAnswer.Request request = new CreateAnswer.Request(1L, 1L, "3");
        CreateAnswer.Response response = new CreateAnswer.Response("3", "3", "test", true, 50D, 50D);

        // when
        when(mockService.submit(request)).thenReturn(response);

        // then
        mockMvc.perform(post("/api/answers/submit")
                        .content(gson.toJson(request))
                        .contentType(APPLICATION_JSON)
                        .characterEncoding(UTF_8))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().json(gson.toJson(response)));
    }
}