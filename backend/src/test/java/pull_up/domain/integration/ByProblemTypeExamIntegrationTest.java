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
import pull_up.domain.exam.dto.*;
import pull_up.domain.member.MemberService;
import pull_up.domain.member.dto.SolvedInfo;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.Member;
import pull_up.infra.database.jpa.fixture.MemberFixture;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
public class ByProblemTypeExamIntegrationTest {

    @Autowired
    ExamRepository examRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ProblemRepository problemRepository;

    ExamService examService;
    MemberService memberService;

    @BeforeEach
    void init() {
        examService = new ExamService(examRepository, memberRepository, problemRepository, null);
        memberService = new MemberService(memberRepository,examRepository,problemRepository, null);
    }

    @Test
    @DisplayName("유형별 시험 통합 테스트")
    void byTypeExamTest() {

        /* 0. 사용자 초기화 */

        Member member = MemberFixture.APPLE_USER.get();

        /* 1. 유형별 페이지 접속 */

        Long memberId = member.getId();
        Entry entry = Entry.MATH;
        SolvedInfo.ByEntryResponse solvedInfoRes = memberService.getSolvedInfo(memberId, entry);

        assertThat(solvedInfoRes.problemTypeCount()).isEqualTo(2);
        assertThat(solvedInfoRes.problemTypeExamInfos()).anySatisfy(problemTypeInfo -> {
            assertThat(problemTypeInfo.isStarted()).isTrue();
            assertThat(problemTypeInfo.totalProblemCount()).isEqualTo(2);
            assertThat(problemTypeInfo.solvedProblemCount()).isEqualTo(2);
            assertThat(problemTypeInfo.incorrectProblemCount()).isEqualTo(1);
            assertThat(problemTypeInfo.correctProblemCount()).isEqualTo(1);
        });

        /* 2. 유형별 시험 시작 */

        Start.ByProblemTypeRequest startReq = new Start.ByProblemTypeRequest(member.getId(), Entry.MATH, "용액의 농도");
        Start.Response startRes = examService.start(startReq);

        assertThat(startRes).isInstanceOf(Start.Response.class);
        assertThat(startRes.examId()).isNotNull();
        assertThat(startRes.totalProblemCount()).isEqualTo(2);
        assertThat(startRes.leftProblemCount()).isEqualTo(1);
        assertThat(startRes.entry()).isEqualTo(Entry.MATH);
        assertThat(startRes.problemNumber()).isEqualTo(1);

        /* 3. 1번 문제 풀기 */

        Submit.Request submitReq1 = new Submit.Request(startRes.examId(), 1, 2);
        Explanation submitRes1 = examService.submit(submitReq1);

        assertThat(submitRes1).isInstanceOf(Explanation.class);
        assertThat(submitRes1.correctAnswer()).isEqualTo(2);
        assertThat(submitRes1.isCorrect()).isEqualTo(true);
        assertThat(submitRes1.correctRate()).isEqualTo(100D);

        /* 4. 다음 문제 요청하기 */

        Next.Response next1Res = examService.next(startRes.examId(), 2);

        assertThat(next1Res).isInstanceOf(Next.Response.class);
        assertThat(next1Res.examId()).isEqualTo(startRes.examId());
        assertThat(next1Res.totalProblemCount()).isEqualTo(2);
        assertThat(next1Res.leftProblemCount()).isEqualTo(0);
        assertThat(next1Res.entry()).isEqualTo(Entry.MATH);
        assertThat(next1Res.problemNumber()).isEqualTo(2);

        /* 5. 2번 문제 풀기 */

        Submit.Request submitReq2 = new Submit.Request(startRes.examId(), 2, 3);
        Explanation submitRes2 = examService.submit(submitReq2);

        assertThat(submitRes2).isInstanceOf(Explanation.class);
        assertThat(submitRes2.correctAnswer()).isEqualTo(1);
        assertThat(submitRes2.isCorrect()).isEqualTo(false);
        assertThat(submitRes2.incorrectRate()).isEqualTo(100D);

        /* 6. 시험 종료 */

        Long endExamId = submitReq2.examId();
        Result.ByEntryResponse endRes = examService.getEntryExamResult(endExamId);

        assertThat(endRes).isInstanceOf(Result.ByEntryResponse.class);
        assertThat(endRes.entry()).isEqualTo(Entry.MATH);
        assertThat(endRes.isFinished()).isEqualTo(true);
        assertThat(endRes.score()).isEqualTo(50);
        assertThat(endRes.results()).hasSize(2);
        assertThat(endRes.results()).noneMatch(problemResult -> !problemResult.isSubmitted());
    }

}
