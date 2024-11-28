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
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.Member;
import pull_up.infra.database.jpa.fixture.MemberFixture;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
public class EvenlyExamIntegrationTest {

    ExamService examService;

    @Autowired
    ExamRepository examRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ProblemRepository problemRepository;

    @BeforeEach
    void init() {
        examService = new ExamService(examRepository, memberRepository, problemRepository, null);
    }

    @Test
    @DisplayName("골고루 시험 통합 테스트")
    void evenlyExamTest() {

        Member member = MemberFixture.APPLE_USER.get();

        /* 1. 시험 시작 */

        // given [프론트] 시험 시작버튼 클릭(POST)
        Start.EvenlyRequest startReq = new Start.EvenlyRequest(member.getId(), Entry.LANGUAGE);

        // when [ExamService] 시험 시작
        Start.Response startRes = examService.start(startReq);

        // then [백] 1번 문제 전달
        assertThat(startRes).isInstanceOf(Start.Response.class);
        assertThat(startRes.examId()).isNotNull();
        assertThat(startRes.totalProblemCount()).isEqualTo(2);
        assertThat(startRes.leftProblemCount()).isEqualTo(1);
        assertThat(startRes.entry()).isEqualTo(Entry.LANGUAGE);
        assertThat(startRes.problemNumber()).isEqualTo(1);

        /* 2. 1번 문제 풀기 */

        // given [프론트] 1번 문제 정답 제출(POST)
        Submit.Request submitReq1 = new Submit.Request(startRes.examId(), 1, 3);

        // when [ExamService] 정답 채점
        Explanation submitRes1 = examService.submit(submitReq1);

        // then [백] 채점 후 결과전송
        assertThat(submitRes1).isInstanceOf(Explanation.class);
        assertThat(submitRes1.correctAnswer()).isEqualTo(2);
        assertThat(submitRes1.isCorrect()).isEqualTo(false);
        assertThat(submitRes1.incorrectRate()).isEqualTo(100D);

        /* 3. 다음 문제 요청하기 */

        // [프론트] 다음 문제 요청(GET)

        // [ExamService] 다음 문제 조회
        Next.Response next1Res = examService.next(startRes.examId(), 2);

        // [백] 다음 문제 전송
        assertThat(next1Res).isInstanceOf(Next.Response.class);
        assertThat(next1Res.examId()).isEqualTo(startRes.examId());
        assertThat(next1Res.totalProblemCount()).isEqualTo(2);
        assertThat(next1Res.leftProblemCount()).isEqualTo(0);
        assertThat(next1Res.entry()).isEqualTo(Entry.LANGUAGE);
        assertThat(next1Res.problemNumber()).isEqualTo(2);

        /* 4. 2번 문제 풀기 */

        // [프론트] 2번문제 정답 제출 및 요청(POST)
        Submit.Request submitReq2 = new Submit.Request(startRes.examId(), 2, 3);

        // [ExamService] 정답 채점
        Explanation submitRes2 = examService.submit(submitReq2);

        // [백] 2번 문제 채점 후 결과 전송
        assertThat(submitRes2).isInstanceOf(Explanation.class);
        assertThat(submitRes2.correctAnswer()).isEqualTo(3);
        assertThat(submitRes2.isCorrect()).isEqualTo(true);
        assertThat(submitRes2.incorrectRate()).isEqualTo(0D);

        /* 5. 2번 조회 */

        Next.Response next2Res = examService.next(startRes.examId(), 2);

        assertThat(next2Res.isSubmitted()).isTrue();
        assertThat(next2Res.explanation()).isNotNull();

        /* 6. 시험 종료 */

        // [프론트] 시험 종료 요청(PATCH)
        Long endExamId = submitReq2.examId();

        // [ExamService] 시험 종료
        End.ByEntryResponse endRes = examService.endByEntryExam(endExamId);

        // [백] 요청결과 전송
        assertThat(endRes).isInstanceOf(End.ByEntryResponse.class);
        assertThat(endRes.entry()).isEqualTo(Entry.LANGUAGE);
        assertThat(endRes.isFinished()).isEqualTo(true);
        assertThat(endRes.score()).isEqualTo(50);
        assertThat(endRes.results()).hasSize(2);
        assertThat(endRes.results()).noneMatch(problemResult -> !problemResult.isSubmitted());
    }

}
