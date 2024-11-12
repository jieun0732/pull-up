package pull_up.api.member.controller;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pull_up.api.member.MemberController;
import pull_up.infra.database.entity.ExamInformation;
import pull_up.infra.database.entity.ExamProblem;
import pull_up.infra.database.repository.exam.ExamInformationRepository;
import pull_up.infra.database.repository.exam.ExamProblemRepository;
import pull_up.infra.database.entity.IncorrectAnswer;
import pull_up.infra.database.entity.Member;
import pull_up.infra.database.entity.MemberAnswer;
import pull_up.infra.database.repository.member.IncorrectAnswerRepository;
import pull_up.infra.database.repository.member.MemberAnswerRepository;
import pull_up.infra.database.repository.member.MemberRepository;
import pull_up.domain.member.MemberService;
import pull_up.infra.database.entity.Problem;
import pull_up.domain.problem.ProblemRepository;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MemberControllerTest {

    MockMvc mockMvc;

    MemberController suit;

    @Mock
    MemberService memberService;

    @BeforeEach
    void init() {
        suit = new MemberController(memberService);
        mockMvc = MockMvcBuilders.standaloneSetup(suit).build();

    }

    @Test
    @DisplayName("회원 삭제 API 테스트")
    void testDeleteMember() throws Exception {
        mockMvc.perform(delete("/api/pull-up/members/" + 1 + "/delete/hard"))
                .andDo(print())
                .andExpect(status().is(200));
    }
}