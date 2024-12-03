package pull_up.domain.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pull_up.config.annotation.IntegrationTest;
import pull_up.domain.dao.ExamRepository;
import pull_up.domain.dao.MemberRepository;
import pull_up.domain.dao.ProblemRepository;
import pull_up.domain.exam.ExamService;
import pull_up.domain.exam.ExamType;
import pull_up.domain.exam.dto.*;
import pull_up.domain.dao.ExamsheetRepository;
import pull_up.domain.examsheet.dto.CreateExamsheet;
import pull_up.domain.examsheet.ExamsheetService;
import pull_up.domain.member.MemberService;
import pull_up.domain.member.dto.SolvedInfo;
import pull_up.domain.problem.Entry;
import pull_up.api.dto.MessageDto;
import pull_up.infra.database.jpa.entity.Examsheet;
import pull_up.infra.database.jpa.entity.Member;
import pull_up.infra.database.jpa.fixture.MemberFixture;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
public class MockExamIntegrationTest {

    @Autowired
    ExamRepository examRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProblemRepository problemRepository;

    @Autowired
    ExamsheetRepository examsheetRepository;

    ExamsheetService examsheetService;
    ExamService examService;
    MemberService memberService;

    @BeforeEach
    void init() {
        examsheetService = new ExamsheetService(examsheetRepository);
        examService = new ExamService(examRepository, memberRepository, problemRepository, examsheetRepository);
        memberService = new MemberService(memberRepository,examRepository,problemRepository);
    }

    @Test
    @DisplayName("모의고사 통합 테스트")
    void testMockExam() {

        Member member = MemberFixture.APPLE_EMAIL_CONCEALED_USER.get();

        /* 1. 시험지 생성(createExamSheet) */

        String examTitle = "모의고사";
        CreateExamsheet.Request createExamsheetReq = new CreateExamsheet.Request(examTitle,List.of(
                new CreateExamsheet.ProblemSheet(1, 5L),
                new CreateExamsheet.ProblemSheet(2, 6L),
                new CreateExamsheet.ProblemSheet(3, 7L),
                new CreateExamsheet.ProblemSheet(4, 8L)));

        MessageDto creatExamsheetRes = examsheetService.createExamsheet(createExamsheetReq);
        Examsheet createdExamsheet = examsheetRepository.findByExamTitle(examTitle);

        assertThat(creatExamsheetRes).isInstanceOf(MessageDto.class);
        assertThat(createdExamsheet.getExamTitle()).isEqualTo(examTitle);
        assertThat(createdExamsheet.getProblemsheets()).hasSize(4);

        /* 모의고사 조회 */

        SolvedInfo.MockExamResponse solvedInfo = memberService.getSolvedInfo(member.getId());
        assertThat(solvedInfo.examId()).isNull();
        assertThat(solvedInfo.isMockExamGraded()).isFalse();

        /* 2. 모의고사 시작(start) */

        Start.MockExamRequest startReq = new Start.MockExamRequest(member.getId(), ExamType.MOCK_EXAM.name());

        Start.Response startRes = examService.start(startReq);

        assertThat(startRes).isInstanceOf(Start.Response.class);
        assertThat(startRes.examId()).isNotNull();
        assertThat(startRes.totalProblemCount()).isEqualTo(4);
        assertThat(startRes.leftProblemCount()).isEqualTo(3);
        assertThat(startRes.entry()).isEqualTo(Entry.REASONING);
        assertThat(startRes.problemNumber()).isEqualTo(1);

        /* 모의고사 재시작(restart) */

        startRes = examService.start(startReq);

        assertThat(startRes).isInstanceOf(Start.Response.class);
        assertThat(startRes.examId()).isNotNull();
        assertThat(startRes.totalProblemCount()).isEqualTo(4);
        assertThat(startRes.leftProblemCount()).isEqualTo(3);
        assertThat(startRes.entry()).isEqualTo(Entry.REASONING);
        assertThat(startRes.problemNumber()).isEqualTo(1);

        /* 모의고사 조회 2 */

        solvedInfo = memberService.getSolvedInfo(member.getId());
        assertThat(solvedInfo.examId()).isEqualTo(startRes.examId());
        assertThat(solvedInfo.isMockExamGraded()).isFalse();

        /* 3. 특정문제 조회(next) */

        Next.Response next = examService.next(startRes.examId(), 3);

        assertThat(next).isInstanceOf(Next.Response.class);
        assertThat(next.examId()).isEqualTo(startRes.examId());
        assertThat(next.totalProblemCount()).isEqualTo(4);
        assertThat(next.leftProblemCount()).isEqualTo(1);
        assertThat(next.problemNumber()).isEqualTo(3);
        assertThat(next.entry()).isEqualTo(Entry.REASONING);

        /* 4. 모의고사 채점(grade) 및 결과 레포트 확인 */

        Grade.Request gradeReq = new Grade.Request(startRes.examId(), List.of(
                new Grade.Request.AnswerSheet(1, 3),
                new Grade.Request.AnswerSheet(2, 3),
                new Grade.Request.AnswerSheet(3, 3),
                new Grade.Request.AnswerSheet(4, 3)));

        Report.mockExam mockExamReportRes = examService.grade(gradeReq);

        assertThat(mockExamReportRes.scoreInfo().myScore()).isEqualTo(25);
        assertThat(mockExamReportRes.durationInfo().myDurationMinute()).isLessThan(1);
        assertThat(mockExamReportRes.vulnerableEntryInfo().vulnerableEntry()).containsExactly(Entry.REASONING.getKorean());

        /* 5. 모의고사 결과 확인(End) */

        Result.MockExamResponse endRes = examService.getMockExamResult(startRes.examId());

        assertThat(endRes.memberName()).isEqualTo(member.getName());
        assertThat(endRes.score()).isEqualTo(25);
        assertThat(endRes.totalProblemCount()).isEqualTo(4);
        assertThat(endRes.correctProblemCount()).isEqualTo(1);
        assertThat(endRes.durationSecond()).isLessThan(1);
        assertThat(endRes.results()).hasSize(4);

        /* 모의고사 조회 3 */

        solvedInfo = memberService.getSolvedInfo(member.getId());
        assertThat(solvedInfo.examId()).isEqualTo(startRes.examId());
        assertThat(solvedInfo.isMockExamGraded()).isTrue();

        /* 6. 모의고사 결과 레포트 조회 */
        Report.mockExam mockExamReport = examService.getMockExamReport(startRes.examId());

        assertThat(mockExamReport).usingRecursiveComparison().isEqualTo(mockExamReportRes);
    }
}
