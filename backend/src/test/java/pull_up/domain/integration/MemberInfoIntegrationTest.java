package pull_up.domain.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pull_up.config.annotation.IntegrationTest;
import pull_up.domain.dao.ExamRepository;
import pull_up.domain.dao.ExamsheetRepository;
import pull_up.domain.dao.MemberRepository;
import pull_up.domain.dao.ProblemRepository;
import pull_up.domain.exam.ExamService;
import pull_up.domain.exam.ExamType;
import pull_up.domain.exam.dto.End;
import pull_up.domain.exam.dto.Grade;
import pull_up.domain.exam.dto.Report;
import pull_up.domain.exam.dto.Start;
import pull_up.domain.member.MemberService;
import pull_up.domain.member.dto.MemberInfo;
import pull_up.infra.database.jpa.entity.Member;
import pull_up.infra.database.jpa.fixture.MemberFixture;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
public class MemberInfoIntegrationTest {

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

    @BeforeEach
    void init() {
        memberService = new MemberService(memberRepository, examRepository, problemRepository);
        examService = new ExamService(examRepository, memberRepository, problemRepository, examsheetRepository);
    }

    @Test
    @DisplayName("사용자 정보조회 통합테스트")
    void testMemberInfo() {

        /* 1. 사용자 정보조회 */

        Member member = MemberFixture.APPLE_EMAIL_CONCEALED_USER.get();

        MemberInfo.Response memberInfoRes = memberService.getMemberInfo(member.getId());

        assertThat(memberInfoRes.memberId()).isEqualTo(member.getId());
        assertThat(memberInfoRes.name()).isEqualTo(member.getName());
        assertThat(memberInfoRes.email()).isEqualTo("CONCEALED_EMAIL");
        assertThat(memberInfoRes.mockExamSolved()).isFalse();
        assertThat(memberInfoRes.mockExamScore()).isZero();

        /* 2. 모의고사 풀기 */

        Start.MockExamRequest startReq = new Start.MockExamRequest(member.getId(), ExamType.MOCK_EXAM.name());

        Start.Response startRes = examService.start(startReq);

        Grade.Request gradeReq = new Grade.Request(startRes.examId(), List.of(
                new Grade.Request.AnswerSheet(1, 3),
                new Grade.Request.AnswerSheet(2, 3),
                new Grade.Request.AnswerSheet(3, 3),
                new Grade.Request.AnswerSheet(4, 3)));

        Report.mockExam gradeRes = examService.grade(gradeReq);

        assertThat(gradeRes.scoreInfo().myScore()).isEqualTo(25);

        /* 3. 사용자 정보조회 */

        memberInfoRes = memberService.getMemberInfo(member.getId());

        assertThat(memberInfoRes.mockExamSolved()).isTrue();
        assertThat(memberInfoRes.mockExamScore()).isEqualTo(25);
    }
}
