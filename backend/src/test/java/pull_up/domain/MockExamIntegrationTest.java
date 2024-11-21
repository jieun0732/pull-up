package pull_up.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pull_up.config.annotation.IntegrationTest;
import pull_up.domain.dao.ExamRepository;
import pull_up.domain.exam.ExamService;
import pull_up.domain.exam.dto.Grade;
import pull_up.domain.exam.dto.Start;
import pull_up.domain.examsheet.dto.CreateExamsheet;
import pull_up.domain.examsheet.ExamsheetService;
import pull_up.global.dto.MessageDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MockExamIntegrationTest {

    ExamsheetService examsheetService;
    ExamService examService;

    @BeforeEach
    void init() {
        examsheetService = new ExamsheetService();
        examService = new ExamService(null, null, null);
    }

    @Test
    @DisplayName("모의고사 통합 테스트")
    void testMockExam() {

        /* 1. 시험지 생성(createExamSheet) */

        // given : [admin] 시험지에 들어갈 문제번호 + 문제 id 목록 전달
        CreateExamsheet.Request createExamsheetReq = new CreateExamsheet.Request(List.of(
                new CreateExamsheet.ProblemSheet(1, 5L),
                new CreateExamsheet.ProblemSheet(2, 6L),
                new CreateExamsheet.ProblemSheet(3, 7L),
                new CreateExamsheet.ProblemSheet(4, 8L)));

        // when : [ExamSheetService] 시험지 객체 생성 : 문제번호, 문제별 id, 시험 제목, 시험 시도횟수, 시험 평균점수
        MessageDto creatExamsheetRes = examsheetService.createExamsheet(createExamsheetReq);

        // then : [backend] messageDto 시험지 객체 생성 성공 메시지
        assertThat(creatExamsheetRes).isInstanceOf(MessageDto.class);

        /* 2. 모의고사 시작(start) */

        // given : [frontend] 모의고사 시작요청 전송
        Start.MockExamRequest startReq = new Start.MockExamRequest(1L);

        // when : [mockExamService] 모의고사 시작
        Start.Response startRes = examService.start(startReq);

        // then : [backend] 전체 문제 전송
        assertThat(startRes).isInstanceOf(Start.Response.class);

        /* 3. 모의고사 채점(grade) */

        // given : [frontend] grade.Request 문제별 답안 전송
        Grade.Request gradeReq = new Grade.Request(1L, List.of(
                new Grade.Request.AnswerSheet(1, 3),
                new Grade.Request.AnswerSheet(2, 3),
                new Grade.Request.AnswerSheet(3, 3),
                new Grade.Request.AnswerSheet(4, 3)));

        // when : [mockExamService] 답안 하나씩 돌며 채점 걸린시간 체크 점수 체크
        Grade.Response gradeRes = examService.grade(gradeReq);

        // then : [backend] grade.Response 채점결과 전송
        assertThat(gradeRes).isInstanceOf(Grade.Response.class);
    }
}
