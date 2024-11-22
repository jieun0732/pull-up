package pull_up.domain.examsheet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pull_up.domain.dao.ExamsheetRepository;
import pull_up.domain.examsheet.dto.CreateExamsheet;
import pull_up.global.dto.MessageDto;
import pull_up.infra.database.jpa.entity.Examsheet;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class ExamsheetServiceTest {

    ExamsheetService suit;

    ExamsheetRepository mockRepository;

    @BeforeEach
    void init() {
        mockRepository = mock(ExamsheetRepository.class);
        suit = new ExamsheetService(mockRepository);
    }

    @Test
    @DisplayName("시험지 생성 테스트")
    void testCreateExamsheet() {
        // given
        CreateExamsheet.Request createExamsheetReq = new CreateExamsheet.Request(
                "test",
                List.of(
                new CreateExamsheet.ProblemSheet(1, 5L),
                new CreateExamsheet.ProblemSheet(2, 6L),
                new CreateExamsheet.ProblemSheet(3, 7L),
                new CreateExamsheet.ProblemSheet(4, 8L)));

        // when
        given(mockRepository.save(any())).willAnswer(answer -> {
            Examsheet examsheet = answer.getArgument(0);
            examsheet.setId(1L);
            return null;
        });

        MessageDto examsheet = suit.createExamsheet(createExamsheetReq);

        // then
        assertThat(examsheet.message()).isEqualTo("시험 문제 [id : 1, 제목 : test] 가 정상 생성되었습니다.");
    }
}