package pull_up.api.member.controller;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import pull_up.infra.database.repository.problem.ProblemRepository;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.ANY,
        connection = EmbeddedDatabaseConnection.H2)
class MemberControllerTest {

    MockMvc mockMvc;
    MemberController suit;
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ExamInformationRepository examInformationRepository;

    @Autowired
    MemberAnswerRepository memberAnswerRepository;

    @Autowired
    IncorrectAnswerRepository incorrectAnswerRepository;

    @Autowired
    ProblemRepository problemRepository;

    @Autowired
    ExamProblemRepository examProblemRepository;

    @Autowired
    EntityManager em;

    // fixtures
    Problem problemFixture;
    Member memberFixture;
    ExamProblem examProblemFixture;
    List<ExamInformation> examInformationFixtures;
    List<IncorrectAnswer> incorrectAnswerFixtures;
    List<MemberAnswer> memberAnswerFixtures;


    @BeforeEach
    void init() {
        memberService = new MemberService(memberRepository);
        suit = new MemberController(memberService);
        mockMvc = MockMvcBuilders.standaloneSetup(suit).build();
        problemFixture = Problem.of("수리",
                "골고루", "속력", "test123", "test1234", "1", "2", "3", "4", "5", "1", "qwer1234", 100, 30, 30d);

        memberFixture = Member.of("test", "test@example.com", false, "apple-user");
        memberFixture.setId(1L);

        examInformationFixtures = List.of(ExamInformation.of(memberFixture, null, "모의고사", null, LocalDateTime.now(), null, null, 0));
        memberAnswerFixtures = List.of(MemberAnswer.of(memberFixture, problemFixture, examInformationFixtures.get(0), "2", false));
        incorrectAnswerFixtures = List.of(IncorrectAnswer.of(memberFixture,problemFixture, examInformationFixtures.get(0), "2", LocalDateTime.now()));

        examProblemFixture = ExamProblem.of(examInformationFixtures.get(0), problemFixture,0L,"1", true);

        memberFixture.setExamInformations(examInformationFixtures);
        memberFixture.setIncorrectAnswers(incorrectAnswerFixtures);
        memberFixture.setMemberAnswers(memberAnswerFixtures);

        examProblemRepository.save(examProblemFixture);
        problemRepository.save(problemFixture);
        memberRepository.save(memberFixture);
        examInformationRepository.save(examInformationFixtures.get(0));
        memberAnswerRepository.save(memberAnswerFixtures.get(0));
        incorrectAnswerRepository.save(incorrectAnswerFixtures.get(0));

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("회원 삭제 API 테스트")
    void testDeleteMember() throws Exception {
        // given
        Long id = 1L;
        URI uri = new URI("/api/pull-up/members/" + id + "/delete/hard");

        // when
        mockMvc.perform(delete(uri))
                .andDo(print())
                .andExpect(status().is(200));

        // then
        assertThat(memberRepository.findById(id)).isEmpty();
        assertThat(examInformationRepository.findById(examInformationFixtures.get(0).getId())).isEmpty();
        assertThat(memberAnswerRepository.findById(memberAnswerFixtures.get(0).getId())).isEmpty();
        assertThat(incorrectAnswerRepository.findById(incorrectAnswerFixtures.get(0).getId())).isEmpty();
        assertThat(examProblemRepository.findById(examProblemFixture.getId())).isEmpty();
    }
}