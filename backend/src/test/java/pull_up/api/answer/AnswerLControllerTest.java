package pull_up.api.answer;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pull_up.api.answer.dto.AnswerDto;
import pull_up.api.answer.dto.AnswerSolved;
import pull_up.api.answer.dto.AnswerSubmit;
import pull_up.domain.deprecated.AnswerServiceL;

import java.util.List;

import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AnswerLControllerTest {

    AnswerServiceL mockService;
    AnswerController suit;
    MockMvc mockMvc;
    Gson gson;

    @BeforeEach
    void init() {
        mockService = Mockito.mock(AnswerServiceL.class);
        suit = new AnswerController(mockService);
        mockMvc = MockMvcBuilders.standaloneSetup(suit).build();
        gson = new Gson();
    }

    @Test
    @DisplayName("POST /submit")
    void testCreateAnswer() throws Exception {
        // given
        AnswerSubmit.Request request = new AnswerSubmit.Request(1L, 1L, "3");
        AnswerSubmit.Response response = new AnswerSubmit.Response(new AnswerDto(3, 3, "test", true, 50D, 50D, null));

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

    @Test
    @DisplayName("GET /api/answers/solved/{id}")
    void testGet() throws Exception {
        // given
        Long id = 1L;
        AnswerDto response = new AnswerDto(3, 3, "test", true, 50D, 50D, null);

        // when
        when(mockService.getSolved(id)).thenReturn(response);

        // then
        mockMvc.perform(get("/api/answers/solved/" + id))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().json(gson.toJson(response)));
    }

    @Test
    @DisplayName("GET /api/answers/solved/entry/{entry}")
    void testSolveAll() throws Exception {
        // given
        String entry = "test";
        Long memberId = 1L;
        AnswerSolved response = new AnswerSolved("test", false, 0, List.of());

        // when
        when(mockService.getSolvedAll(memberId, entry)).thenReturn(response);
        System.out.println(gson.toJson(response));

        // then
        mockMvc.perform(get("/api/answers/solved/entry/" + encode(entry, UTF_8))
                        .queryParam("memberId", String.valueOf(memberId)))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(content().json(gson.toJson(response)));
    }
}