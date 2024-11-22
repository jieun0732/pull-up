package pull_up.infra.database.jpa.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pull_up.infra.database.jpa.fixture.FixtureRepository;

import java.time.Duration;
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

    @Test
    @DisplayName("시험지 표시 테스트")
    void testMark() {
        // given
        Examsheet examsheet = FixtureRepository.getExamsheet("모의고사");

        // when
        examsheet.mark(30, Duration.ofSeconds(10));

        // then
        assertThat(examsheet.getExamCount()).isEqualTo(1);
        assertThat(examsheet.getAverageScore()).isEqualTo(30);
        assertThat(examsheet.getAverageDuration()).isEqualTo(Duration.ofSeconds(10));

        // when 2
        examsheet.mark(50, Duration.ofSeconds(20));

        // then 2
        assertThat(examsheet.getExamCount()).isEqualTo(2);
        assertThat(examsheet.getAverageScore()).isEqualTo(40);
        assertThat(examsheet.getAverageDuration()).isEqualTo(Duration.ofSeconds(15));

        // when 3
        examsheet.mark(0, Duration.ofSeconds(0));

        // then 2
        assertThat(examsheet.getExamCount()).isEqualTo(3);
        assertThat(examsheet.getAverageScore()).isEqualTo((double) 80 / 3);
        assertThat(examsheet.getAverageDuration()).isEqualTo(Duration.ofSeconds(10));
    }

}