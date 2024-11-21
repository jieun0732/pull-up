package pull_up.infra.database.jpa.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ExamsheetTest {

    @Test
    @DisplayName("시험지 생성 테스트")
    void testCreateExamsheet() {
        // given
        String examTitle = "모의고사";
        Map<Integer, Long> problemMap = new HashMap<>();
        problemMap.put(1, 1L);
        problemMap.put(2, 2L);
        problemMap.put(3, 3L);

        // when
        Examsheet examsheet = Examsheet.create(examTitle, problemMap);

        // then
        assertThat(examsheet.getExamTitle()).isEqualTo(examTitle);
        assertThat(examsheet.getProblemsheets()).hasSize(3)
                .anySatisfy(problemsheet -> {
                    assertThat(problemsheet.getProblemNumber()).isEqualTo(1);
                    assertThat(problemsheet.getProblemId()).isEqualTo(1L);
                }).anySatisfy(problemsheet -> {
                    assertThat(problemsheet.getProblemNumber()).isEqualTo(2);
                    assertThat(problemsheet.getProblemId()).isEqualTo(2L);
                }).anySatisfy(problemsheet -> {
                    assertThat(problemsheet.getProblemNumber()).isEqualTo(3);
                    assertThat(problemsheet.getProblemId()).isEqualTo(3L);
                });
    }

}