package pull_up.global.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalFormatterTest {

    @Test
    @DisplayName("한국 날짜 포맷 테스트")
    void testKoreanDateFormatter() {
        // given
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.of(2024, 12, 6), LocalTime.of(13, 44));

        // when
        String format = dateTime.format(GlobalFormatter.KOREAN_DATE_FORMATTER);

        // then
        assertThat(format).isEqualTo("12월 06일");
    }

    @Test
    @DisplayName("한국 시간 포맷 테스트")
    void testKoreanTimeFormatter() {
        // given
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.of(2024, 12, 6), LocalTime.of(13, 44));

        // when
        String format = dateTime.format(GlobalFormatter.KOREAN_TIME_FORMATTER);

        // then
        assertThat(format).isEqualTo("12월 06일 13시 44분");
    }

    @Test
    @DisplayName("소요시간 포맷 테스트")
    void testFormatMinuteSecond() {
        // given
        Duration duration = Duration.of(123, ChronoUnit.SECONDS);

        // when
        String format = GlobalFormatter.formatMinuteSecond(duration);

        // then
        assertThat(format).isEqualTo("02:03");
    }
}