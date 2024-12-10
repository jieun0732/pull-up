package pull_up.domain.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pull_up.api.dto.MessageDto;
import pull_up.config.annotation.IntegrationTest;
import pull_up.domain.dao.ExamRepository;
import pull_up.domain.dao.ExamsheetRepository;
import pull_up.domain.dao.MemberRepository;
import pull_up.domain.dao.ProblemRepository;
import pull_up.domain.exam.ExamService;
import pull_up.domain.exam.dto.*;
import pull_up.domain.examsheet.ExamsheetService;
import pull_up.domain.examsheet.dto.CreateExamsheet;
import pull_up.domain.member.MemberService;
import pull_up.domain.member.dto.SolvedInfo;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.embedded.Problemsheet;
import pull_up.infra.database.jpa.entity.Examsheet;
import pull_up.infra.database.jpa.entity.Member;
import pull_up.infra.database.jpa.entity.Problem;
import pull_up.infra.database.jpa.fixture.FixtureRepository;
import pull_up.infra.database.jpa.fixture.MemberFixture;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
public class ByProblemTypeExamIntegrationTestV2 {

    @Autowired
    ExamRepository examRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ProblemRepository problemRepository;

    @Autowired
    ExamsheetRepository examsheetRepository;

    ExamService examService;
    MemberService memberService;
    ExamsheetService examsheetService;

    @BeforeEach
    void init() {
        examService = new ExamService(examRepository, memberRepository, problemRepository, null);
        memberService = new MemberService(memberRepository,examRepository,problemRepository, null);
        examsheetService = new ExamsheetService(examsheetRepository, problemRepository);

    }

    @Test
    @DisplayName("유형별 시험 통합 테스트")
    void byTypeExamTest() {

        /* 0. 사용자 초기화 */
        Member member = MemberFixture.APPLE_USER.get();
        String examTitle = "EVENLY_MATH";

        /* 1. 시험지 생성 */

        CreateExamsheet.Request createExamsheetReq = new CreateExamsheet.Request(examTitle, List.of(
                new CreateExamsheet.ProblemSheet(1, 4L)));

        MessageDto creatExamsheetRes = examsheetService.createExamsheet(createExamsheetReq);
        Examsheet createdExamsheet = examsheetRepository.findByExamTitle(examTitle);

        assertThat(creatExamsheetRes).isInstanceOf(MessageDto.class);
        assertThat(createdExamsheet.getExamTitle()).isEqualTo(examTitle);
        assertThat(createdExamsheet.getProblemsheets()).hasSize(1);

        /* 2. 시험 조회 시 시험지에 사용된 문제는 조회되지 않음 */

        SolvedInfo.ByEntryResponse solvedInfoRes = memberService.getSolvedInfo(member.getId(), Entry.MATH);
        assertThat(solvedInfoRes.problemTypeCount()).isEqualTo(2);
        assertThat(solvedInfoRes.problemTypeExamInfos()).anySatisfy(problemTypeInfo -> {
            assertThat(problemTypeInfo.isStarted()).isFalse();
            assertThat(problemTypeInfo.totalProblemCount()).isEqualTo(1);
            assertThat(problemTypeInfo.solvedProblemCount()).isEqualTo(0);
            assertThat(problemTypeInfo.incorrectProblemCount()).isEqualTo(0);
            assertThat(problemTypeInfo.correctProblemCount()).isEqualTo(0);
        });

        /* 3. 시험 생성 시 시험지에 사용된 문제는 생성되지 않음 */

        Start.ByProblemTypeRequest startReq = new Start.ByProblemTypeRequest(member.getId(), Entry.MATH, "용액의 농도");
        Start.Response startRes = examService.start(startReq);
        Problemsheet problemsheet = createdExamsheet.getProblemsheets().get(0);
        Problem problemInSheet = FixtureRepository.getProblemById(problemsheet.getProblemId());

        assertThat(startRes).isInstanceOf(Start.Response.class);
        assertThat(startRes.examId()).isNotNull();
        assertThat(startRes.totalProblemCount()).isEqualTo(1);
        assertThat(startRes.leftProblemCount()).isEqualTo(0);
        assertThat(startRes.entry()).isEqualTo(Entry.MATH);
        assertThat(startRes.problemNumber()).isEqualTo(1);
        assertThat(startRes.question()).isNotEqualTo(problemInSheet.getQuestionSummary());
    }

}
