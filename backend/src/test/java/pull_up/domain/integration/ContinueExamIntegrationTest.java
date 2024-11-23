package pull_up.domain.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pull_up.api.dto.MessageDto;
import pull_up.config.annotation.IntegrationTest;
import pull_up.domain.dao.ExamRepository;
import pull_up.domain.dao.MemberRepository;
import pull_up.domain.dao.ProblemRepository;
import pull_up.domain.exam.ExamService;
import pull_up.domain.exam.dto.Next;
import pull_up.domain.exam.dto.Start;
import pull_up.domain.exam.dto.Submit;
import pull_up.domain.exam.exception.ExamErrorCode;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.Member;
import pull_up.infra.database.jpa.fixture.MemberFixture;

import static org.assertj.core.api.Assertions.*;

@IntegrationTest
public class ContinueExamIntegrationTest {

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
    @DisplayName("시험 이어하기 통합 테스트")
    void testContinueExam() {

        Member member = MemberFixture.APPLE_USER.get();

        /* 1. 시험 시작 */

        Start.EvenlyRequest startReq = new Start.EvenlyRequest(member.getId(), Entry.LANGUAGE);

        Start.Response startRes = examService.start(startReq);

        assertThat(startRes).isInstanceOf(Start.Response.class);
        assertThat(startRes.examId()).isNotNull();
        assertThat(startRes.totalProblemCount()).isEqualTo(2);
        assertThat(startRes.leftProblemCount()).isEqualTo(1);
        assertThat(startRes.entry()).isEqualTo(Entry.LANGUAGE);
        assertThat(startRes.problemNumber()).isEqualTo(1);

        /* 2. 시험 1개 풀이 */

        Submit.Request submitReq1 = new Submit.Request(startRes.examId(), 1, 3);

        Submit.Response submitRes1 = examService.submit(submitReq1);

        assertThat(submitRes1).isInstanceOf(Submit.Response.class);
        assertThat(submitRes1.correctAnswer()).isEqualTo(2);
        assertThat(submitRes1.isCorrect()).isEqualTo(false);
        assertThat(submitRes1.incorrectRate()).isEqualTo(100D);

        /* 3. 시험 이어풀기 */

        Long examId = startRes.examId();

        Next.Response continueRes = examService.continueExam(examId);

        assertThat(continueRes).isInstanceOf(Next.Response.class);
        assertThat(continueRes.examId()).isEqualTo(examId);
        assertThat(continueRes.leftProblemCount()).isEqualTo(0);

        /* 4. 시험 리셋 */

        MessageDto messageRes = examService.reset(examId);

        assertThat(messageRes.message()).isEqualTo(examId + "번 시험이 리셋되었습니다.");

        /* 5. 시험 시작하기 */

        Start.Response startRes2 = examService.start(startReq);

        Long examId2 = startRes2.examId();
        assertThat(examId2).isNotEqualTo(startRes.examId());

        /* 6. 시험 전체 풀이 */

        Submit.Request submitReq2 = new Submit.Request(examId2, 1, 3);
        Submit.Request submitReq3 = new Submit.Request(examId2, 2, 3);

        examService.submit(submitReq2);
        Submit.Response submitRes2 = examService.submit(submitReq3);

        assertThat(submitRes2).isInstanceOf(Submit.Response.class);
        assertThat(submitRes2.correctAnswer()).isEqualTo(3);
        assertThat(submitRes2.isCorrect()).isEqualTo(true);
        assertThat(submitRes2.incorrectRate()).isZero();

        /* 7. 시험 종료 후 시험 이어풀기 */

        assertThatThrownBy(() -> examService.continueExam(examId2)).hasMessage(ExamErrorCode.PROBLEM_NUMBER_EXCEED.getMessage());

    }
}
