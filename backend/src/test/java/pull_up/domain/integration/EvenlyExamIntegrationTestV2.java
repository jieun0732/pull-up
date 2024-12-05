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
import pull_up.domain.exam.dto.Explanation;
import pull_up.domain.exam.dto.Result;
import pull_up.domain.exam.dto.Start;
import pull_up.domain.exam.dto.Submit;
import pull_up.domain.examsheet.ExamsheetService;
import pull_up.domain.examsheet.dto.CreateExamsheet;
import pull_up.domain.member.MemberService;
import pull_up.domain.member.dto.SolvedInfo;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.Examsheet;
import pull_up.infra.database.jpa.entity.Member;
import pull_up.infra.database.jpa.fixture.MemberFixture;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
public class EvenlyExamIntegrationTestV2 {

    ExamsheetService examsheetService;
    ExamService examService;
    MemberService memberService;

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
        examService = new ExamService(examRepository, memberRepository, problemRepository, examsheetRepository);
        examsheetService = new ExamsheetService(examsheetRepository, problemRepository);
        memberService = new MemberService(memberRepository,examRepository,problemRepository);
    }

    @Test
    @DisplayName("골고루 시험 통합 테스트 V2")
    void evenlyExamTest() {

        /* 0. 사용자 및 시험 제목 초기화*/
        Member member = MemberFixture.APPLE_EMAIL_CONCEALED_USER.get();
        String examTitle = "EVENLY_LANGUAGE";

        /* 1. 시험지 생성 */

        CreateExamsheet.Request createExamsheetReq = new CreateExamsheet.Request(examTitle, List.of(
                new CreateExamsheet.ProblemSheet(1, 9L),
                new CreateExamsheet.ProblemSheet(2, 11L)));

        MessageDto creatExamsheetRes = examsheetService.createExamsheet(createExamsheetReq);
        Examsheet createdExamsheet = examsheetRepository.findByExamTitle(examTitle);

        assertThat(creatExamsheetRes).isInstanceOf(MessageDto.class);
        assertThat(createdExamsheet.getExamTitle()).isEqualTo(examTitle);
        assertThat(createdExamsheet.getProblemsheets()).hasSize(2);

        /* 2. 시험 조회 */

        SolvedInfo.ByEntryResponse solvedInfo = memberService.getSolvedInfo(member.getId(), Entry.LANGUAGE);
        assertThat(solvedInfo.entry()).isEqualTo(Entry.LANGUAGE);
        assertThat(solvedInfo.evenlyExamInfo().examId()).isNull();
        assertThat(solvedInfo.evenlyExamInfo().isStarted()).isFalse();

        /* 3. 시험 시작 */

        Start.EvenlyRequestV2 startReq = new Start.EvenlyRequestV2(member.getId(), examTitle, Entry.LANGUAGE);
        Start.Response startRes = examService.startV2(startReq);

        assertThat(startRes).isInstanceOf(Start.Response.class);
        assertThat(startRes.examId()).isNotNull();
        assertThat(startRes.totalProblemCount()).isEqualTo(2);
        assertThat(startRes.leftProblemCount()).isEqualTo(1);
        assertThat(startRes.entry()).isEqualTo(Entry.LANGUAGE);
        assertThat(startRes.problemNumber()).isEqualTo(1);

        /* 4. 시험 조회 */

        solvedInfo = memberService.getSolvedInfo(member.getId(), Entry.LANGUAGE);
        assertThat(solvedInfo.entry()).isEqualTo(Entry.LANGUAGE);
        assertThat(solvedInfo.evenlyExamInfo().examId()).isNotNull();
        assertThat(solvedInfo.evenlyExamInfo().isStarted()).isTrue();
        assertThat(solvedInfo.evenlyExamInfo().lastSolvedProblemNumber()).isEqualTo(0);

        /* 5. 1번 문제 풀기 */

        Submit.Request submitReq1 = new Submit.Request(startRes.examId(), 1, 3);
        Explanation submitRes1 = examService.submit(submitReq1);

        assertThat(submitRes1).isInstanceOf(Explanation.class);
        assertThat(submitRes1.correctAnswer()).isEqualTo(2);
        assertThat(submitRes1.isCorrect()).isEqualTo(false);
        assertThat(submitRes1.incorrectRate()).isEqualTo(100D);

        /* 6. 시험 조회 */

        solvedInfo = memberService.getSolvedInfo(member.getId(), Entry.LANGUAGE);
        assertThat(solvedInfo.entry()).isEqualTo(Entry.LANGUAGE);
        assertThat(solvedInfo.evenlyExamInfo().examId()).isNotNull();
        assertThat(solvedInfo.evenlyExamInfo().isStarted()).isTrue();
        assertThat(solvedInfo.evenlyExamInfo().lastSolvedProblemNumber()).isEqualTo(1);

        /* 7. 2번 문제 풀기 */

        Submit.Request submitReq2 = new Submit.Request(startRes.examId(), 2, 3);
        Explanation submitRes2 = examService.submit(submitReq2);

        assertThat(submitRes2).isInstanceOf(Explanation.class);
        assertThat(submitRes2.correctAnswer()).isEqualTo(3);
        assertThat(submitRes2.isCorrect()).isEqualTo(true);
        assertThat(submitRes2.incorrectRate()).isEqualTo(0D);

        /* 8. 시험 종료 */

        Long endExamId = submitReq2.examId();
        Result.ByEntryResponse endRes = examService.getEntryExamResult(endExamId);

        assertThat(endRes).isInstanceOf(Result.ByEntryResponse.class);
        assertThat(endRes.entry()).isEqualTo(Entry.LANGUAGE);
        assertThat(endRes.isFinished()).isEqualTo(true);
        assertThat(endRes.score()).isEqualTo(50);
        assertThat(endRes.results()).hasSize(2);
        assertThat(endRes.results()).noneMatch(problemResult -> !problemResult.isSubmitted());
    }
}
