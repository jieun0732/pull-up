package pull_up.domain.integration;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pull_up.api.dto.ListDto;
import pull_up.config.annotation.IntegrationTest;
import pull_up.domain.dao.ExamRepository;
import pull_up.domain.dao.ExamsheetRepository;
import pull_up.domain.dao.MemberRepository;
import pull_up.domain.dao.ProblemRepository;
import pull_up.domain.exam.ExamService;
import pull_up.domain.exam.ExamType;
import pull_up.domain.exam.dto.*;
import pull_up.domain.examsheet.ExamsheetService;
import pull_up.domain.examsheet.dto.CreateExamsheet;
import pull_up.domain.member.MemberService;
import pull_up.infra.database.jpa.dto.IncorrectQueryDto;
import pull_up.domain.problem.Entry;
import pull_up.infra.database.jpa.entity.Member;
import pull_up.infra.database.jpa.fixture.MemberFixture;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
public class IncorrectAnswerIntegrationTest {

    MemberService memberService;
    ExamService examService;
    ExamsheetService examsheetService;


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
        examsheetService = new ExamsheetService(examsheetRepository, problemRepository);
    }

    @Test
    @DisplayName("틀린문제 조회 통합테스트")
    void testIncorrectAnswer() {

        /* 0. 멤버 및 시험 초기화 */

        Member member = MemberFixture.APPLE_EMAIL_CONCEALED_USER.get();
        String examTitle = "EVENLY_LANGUAGE";

        CreateExamsheet.Request createExamsheetReq = new CreateExamsheet.Request(examTitle, List.of(
                new CreateExamsheet.ProblemSheet(1, 9L),
                new CreateExamsheet.ProblemSheet(2, 11L)));

        examsheetService.createExamsheet(createExamsheetReq);

        /* 1. 사용자 틀린문제 조회 */


        ListDto<IncorrectQueryDto> incorrectRes = memberService.getIncorrect(member.getId());

        assertThat(incorrectRes).isInstanceOf(ListDto.class);
        assertThat(incorrectRes.list()).hasSize(0);

        /* 2. 골고루 문제 1개 틀리기 */

        Start.EvenlyRequest startReq = new Start.EvenlyRequest(member.getId(), Entry.LANGUAGE);
        Start.Response startRes = examService.start(startReq);

        Submit.Request submitReq1 = new Submit.Request(startRes.examId(), 1, 3);
        Explanation explanation1 = examService.submit(submitReq1);
        Submit.Request submitReq2 = new Submit.Request(startRes.examId(), 2, 3);
        Explanation explanation2 = examService.submit(submitReq2);

        assertThat(explanation1.isCorrect()).isFalse();
        assertThat(explanation2.isCorrect()).isTrue();

        /* 3. 사용자 틀린문제 조회(골고루 틀린문제 1개 추가) */

        incorrectRes = memberService.getIncorrect(member.getId());

        assertThat(incorrectRes.list()).hasSize(1);
        assertThat(incorrectRes.list()).allSatisfy(response -> {
                    assertThat(response.getEntry()).isEqualTo(Entry.LANGUAGE);
                    assertThat(response.getProblemNumber()).isEqualTo(submitReq1.problemNumber());
                }
        );

        /* 4. 유형별 문제 1개 틀리기 */

        Start.ByProblemTypeRequest startReq2 = new Start.ByProblemTypeRequest(member.getId(), Entry.MATH, "용액의 농도");
        Start.Response startRes2 = examService.start(startReq2);

        Submit.Request submitReq3 = new Submit.Request(startRes2.examId(), 1, 3);
        Explanation explanation3 = examService.submit(submitReq3);
        Submit.Request submitReq4 = new Submit.Request(startRes2.examId(), 2, 1);
        Explanation explanation4 = examService.submit(submitReq4);

        assertThat(explanation3.isCorrect()).isFalse();
        assertThat(explanation4.isCorrect()).isTrue();

        /* 5. 사용자 틀린문제 조회(유형별 틀린문제 1개 추가) */

        incorrectRes = memberService.getIncorrect(member.getId());

        assertThat(incorrectRes.list()).hasSize(2);
        assertThat(incorrectRes.list()).anySatisfy(response -> {
            assertThat(response.getEntry()).isEqualTo(Entry.LANGUAGE);
            assertThat(response.getProblemNumber()).isEqualTo(submitReq1.problemNumber());
        }).anySatisfy(response -> {
            assertThat(response.getEntry()).isEqualTo(Entry.MATH);
            assertThat(response.getProblemNumber()).isEqualTo(submitReq3.problemNumber());
        });

        /* 6. 모의고사 풀기 */

        Start.MockExamRequest startReq3 = new Start.MockExamRequest(member.getId(), ExamType.MOCK_EXAM.name());
        Start.Response startRes3 = examService.start(startReq3);

        Grade.Request gradeReq = new Grade.Request(startRes3.examId(), List.of(
                new Grade.Request.AnswerSheet(1, 3),
                new Grade.Request.AnswerSheet(2, 3),
                new Grade.Request.AnswerSheet(3, 3),
                new Grade.Request.AnswerSheet(4, 3)));
        Report.mockExam gradeRes = examService.grade(gradeReq);

        assertThat(gradeRes.scoreInfo().myScore()).isEqualTo(25);

        /* 7. 사용자 틀린문제 조회(모의고사 틀린문제는 조회하지 않으니 변경X) */

        incorrectRes = memberService.getIncorrect(member.getId());

        assertThat(incorrectRes.list()).hasSize(2);
        assertThat(incorrectRes.list()).anySatisfy(response -> {
            assertThat(response.getEntry()).isEqualTo(Entry.LANGUAGE);
            assertThat(response.getProblemNumber()).isEqualTo(submitReq1.problemNumber());
        }).anySatisfy(response -> {
            assertThat(response.getEntry()).isEqualTo(Entry.MATH);
            assertThat(response.getProblemNumber()).isEqualTo(submitReq3.problemNumber());
        });
    }
}
