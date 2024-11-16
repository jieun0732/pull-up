package pull_up.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pull_up.api.exam.evenly.dto.End;
import pull_up.api.exam.evenly.dto.Next;
import pull_up.api.exam.evenly.dto.Start;
import pull_up.api.exam.evenly.dto.Submit;
import pull_up.domain.answer.AnswerService;
import pull_up.domain.exam.EvenlyExamService;
import pull_up.domain.exam.ExamType;
import pull_up.domain.problem.Entry;

import static org.assertj.core.api.Assertions.assertThat;

public class EvenlyExamIntegrationTest {

    EvenlyExamService evenlyExamService;
    AnswerService answerService;

    @BeforeEach
    void init() {
        evenlyExamService = new EvenlyExamService(null, null, null, null);
        answerService = new AnswerService();
    }

    @Test
    @DisplayName("골고루 시험 통합 테스트")
    void evenlyExamTest() {

        /* 1. 시험 시작 */

        // [프론트] 시험 시작버튼 클릭(POST)
        Start.Request startReq = new Start.Request(1L, ExamType.EVENLY, Entry.LANGUAGE);

        // [ExamService] 시험 시작
        Start.Response startRes = evenlyExamService.start(startReq);

        // [백] 1번 문제 전달
        // TODO : MySQL 테스트용 DB 넣고 SQL문으로 가짜 데이터 삽입하기 -> Repository 연동해서 통합테스트 진행
        assertThat(startRes).isInstanceOf(Start.Response.class);
        assertThat(startRes.entry()).isEqualTo(Entry.LANGUAGE);
        assertThat(startRes.problemNumber()).isEqualTo(1);

        /* 2. 1번 문제 풀기 */

        // [프론트] 1번 문제 정답 제출(POST)
        Submit.Request submitReq1 = new Submit.Request();

        // [ExamService] 정답 채점
        Submit.Response submitRes1 = evenlyExamService.submit(submitReq1);

        // [백] 채점 후 결과전송
        assertThat(submitRes1).isNotNull();

        /* 3. 다음 문제 요청하기 */

        // [프론트] 다음 문제 요청(GET)
        Long memberId = 1L;
        Long examId = 1L;
        Long answerId = 1L;

        // [ExamService] 다음 문제 조회
        Next.Response next1Res = evenlyExamService.next(memberId, examId, answerId);

        // [백] 다음 문제 전송
        assertThat(next1Res).isNotNull();

        /* 4. 2 ~ 5번 문제 풀기 */

        // [프론트] 2 ~ 5번문제 정답 제출 및 요청(POST)
        Submit.Request submitReq2 = new Submit.Request();
        Submit.Request submitReq3 = new Submit.Request();
        Submit.Request submitReq4 = new Submit.Request();
        Submit.Request submitReq5 = new Submit.Request();

        // [ExamService] 정답 채점
        evenlyExamService.submit(submitReq2);
        evenlyExamService.submit(submitReq3);
        evenlyExamService.submit(submitReq4);
        evenlyExamService.submit(submitReq5);

        /* 5. 시험 종료 */

        // [프론트] 시험 종료 요청(PATCH)
        End.Request endReq = new End.Request();

        // [ExamService] 시험 종료
        evenlyExamService.end(endReq);

        /* 6. 푼 문제 조회 */

        // [프론트] 푼문제 조회(GET)
        memberId = 1L;

        // [AnswerService] 푼 문제 조회
        Object answerRes = answerService.getSolved(memberId);

        // [백] 푼 문제 중 맞은문제 / 틀린문제 현황 조회해서 전달
        assertThat(answerRes).isNotNull();
    }

}
