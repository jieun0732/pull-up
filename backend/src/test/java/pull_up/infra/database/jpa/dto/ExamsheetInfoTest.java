package pull_up.infra.database.jpa.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ExamsheetInfoTest {

    @Test
    @DisplayName("쿼리로 받아온 정보 변환 테스트")
    void testGetInfoByQuery() {
        // given
        ExamsheetInfo goal = new ExamsheetInfo(1L, "11월 29일", "11월 29일", "MOCK_EXAM", 20, 0, "80.1", "12:00");

        // when
        ExamsheetInfo suit = new ExamsheetInfo(1L, LocalDateTime.of(LocalDate.of(2024, 11, 29), LocalTime.now()), LocalDateTime.of(LocalDate.of(2024, 11, 29), LocalTime.now()), "MOCK_EXAM", 20, 0, 80.123D, Duration.of(12, ChronoUnit.MINUTES));

        // then
        assertThat(goal).usingRecursiveComparison().isEqualTo(suit);
    }
}