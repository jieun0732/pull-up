package pull_up.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pull_up.domain.exam.dto.End;
import pull_up.domain.exam.dto.Next;
import pull_up.domain.exam.dto.Start;
import pull_up.domain.exam.dto.Submit;
import pull_up.config.annotation.IntegrationTest;
import pull_up.domain.answer.AnswerRepository;
import pull_up.domain.answer.AnswerService;
import pull_up.domain.exam.EvenlyExamService;
import pull_up.domain.exam.ExamRepository;
import pull_up.domain.exam.ExamType;
import pull_up.domain.member.MemberRepository;
import pull_up.domain.problem.Entry;
import pull_up.domain.problem.ProblemRepository;
import pull_up.infra.database.entity.Member;
import pull_up.infra.database.fixture.MemberFixture;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
public class EvenlyExamIntegrationTest {

    EvenlyExamService evenlyExamService;
    AnswerService answerService;

    @Autowired ExamRepository examRepository;
    @Autowired AnswerRepository answerRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired ProblemRepository problemRepository;

    @BeforeEach
    void init() {
        evenlyExamService = new EvenlyExamService(examRepository, answerRepository, memberRepository, problemRepository);
        answerService = new AnswerService();
    }

    @Test
    @DisplayName("골고루 시험 통합 테스트")
    void evenlyExamTest() {

        Member member = MemberFixture.APPLE_USER.get();

        /* 1. 시험 시작 */

        // given [프론트] 시험 시작버튼 클릭(POST)
        Start.Request startReq = new Start.Request(member.getId(), ExamType.EVENLY, Entry.LANGUAGE);

        // when [ExamService] 시험 시작
        Start.Response startRes = evenlyExamService.start(startReq);

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
        Submit.Response submitRes1 = evenlyExamService.submit(submitReq1);

        // then [백] 채점 후 결과전송
        assertThat(submitRes1).isInstanceOf(Submit.Response.class);
        assertThat(submitRes1.correctAnswer()).isEqualTo(2);
        assertThat(submitRes1.isCorrect()).isEqualTo(false);

        /* 3. 다음 문제 요청하기 */

        // [프론트] 다음 문제 요청(GET)

        // [ExamService] 다음 문제 조회
        Next.Response next1Res = evenlyExamService.next(startRes.examId(), 2);

        // [백] 다음 문제 전송
        assertThat(next1Res).isInstanceOf(Next.Response.class);
        assertThat(next1Res.examId()).isEqualTo(startRes.examId());
        assertThat(next1Res.totalProblemCount()).isEqualTo(2);
        assertThat(next1Res.leftProblemCount()).isEqualTo(0);
        assertThat(next1Res.entry()).isEqualTo(Entry.LANGUAGE);
        assertThat(next1Res.problemNumber()).isEqualTo(2);

        /* 4. 2번 문제 풀기 */

        // [프론트] 2번문제 정답 제출 및 요청(POST)
        Submit.Request submitReq2 = new Submit.Request(1L, 2, 3);

        // [ExamService] 정답 채점
        evenlyExamService.submit(submitReq2);

        /* 5. 시험 종료 */

        // [프론트] 시험 종료 요청(PATCH)
        End.Request endReq = new End.Request();

        // [ExamService] 시험 종료
        evenlyExamService.end(endReq);

        /* 6. 푼 문제 조회 */

        // [프론트] 푼문제 조회(GET)

        // [AnswerService] 푼 문제 조회
        Object answerRes = answerService.getSolved(member.getId());

        // [백] 푼 문제 중 맞은문제 / 틀린문제 현황 조회해서 전달
        assertThat(answerRes).isNotNull();
    }

}
