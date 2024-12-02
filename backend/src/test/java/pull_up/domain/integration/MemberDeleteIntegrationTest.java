package pull_up.domain.integration;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pull_up.api.dto.MessageDto;
import pull_up.config.annotation.IntegrationTest;
import pull_up.domain.auth.service.OAuth2LoginService;
import pull_up.domain.dao.ExamRepository;
import pull_up.domain.dao.ExamsheetRepository;
import pull_up.domain.dao.MemberRepository;
import pull_up.domain.dao.ProblemRepository;
import pull_up.domain.exam.ExamService;
import pull_up.domain.exam.ExamType;
import pull_up.domain.exam.dto.Grade;
import pull_up.domain.exam.dto.Report;
import pull_up.domain.exam.dto.Start;
import pull_up.domain.member.MemberService;
import pull_up.infra.database.jpa.entity.Exam;
import pull_up.infra.database.jpa.entity.Member;
import pull_up.infra.database.jpa.fixture.MemberFixture;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
public class MemberDeleteIntegrationTest {

    MemberService memberService;
    ExamService examService;

    @Autowired
    ExamRepository examRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ProblemRepository problemRepository;
    @Autowired
    ExamsheetRepository examsheetRepository;

    @Autowired
    EntityManager em;

    @BeforeEach
    void init() {
        memberService = new MemberService(memberRepository, examRepository, problemRepository);
        examService = new ExamService(examRepository, memberRepository, problemRepository, examsheetRepository);
    }

    @Test
    @DisplayName("사용자 탈퇴 통합테스트")
    void testDeleteMember() {

        Member member = MemberFixture.APPLE_USER.get();

        /* 1. 사용자 모의고사 풀기 */
        Start.MockExamRequest startReq = new Start.MockExamRequest(member.getId(), ExamType.MOCK_EXAM.name());
        Start.Response startRes = examService.start(startReq);
        assertThat(startRes.totalProblemCount()).isEqualTo(4);

        Grade.Request gradeReq = new Grade.Request(startRes.examId(), List.of(
                new Grade.Request.AnswerSheet(1, 3),
                new Grade.Request.AnswerSheet(2, 3),
                new Grade.Request.AnswerSheet(3, 3),
                new Grade.Request.AnswerSheet(4, 3)));
        Report.mockExam gradeRes = examService.grade(gradeReq);
        assertThat(gradeRes.scoreInfo().myScore()).isEqualTo(25);

        /* 2. 사용자 회원탈퇴 : 회원정보 전부 삭제 */
        MessageDto messageDto = memberService.delete(member.getId());
        assertThat(messageDto.message()).isEqualTo("회원탈퇴가 완료되었습니다.");
        assertThat(memberRepository.findById(member.getId())).isEmpty();
        em.flush();
        em.clear();

        /* 3. 모의고사 정보 조회 : 모의고사 정보는 삭제되지 않음 */
        Exam exam = examRepository.findById(startRes.examId()).get();
        assertThat(exam.getMember().getId()).isEqualTo(-1L);
        assertThat(exam.getScore()).isEqualTo(25);
    }
}
