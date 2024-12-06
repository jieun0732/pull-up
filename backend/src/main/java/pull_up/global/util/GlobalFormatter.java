package pull_up.global.util;

import lombok.Getter;

import java.time.Duration;
import java.time.format.DateTimeFormatter;

@Getter
public class GlobalFormatter {

    public static final DateTimeFormatter KOREAN_DATE_FORMATTER = DateTimeFormatter.ofPattern("MM월 dd일");
    public static final DateTimeFormatter KOREAN_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM월 dd일 HH시 mm분");

    public static String formatMinuteSecond(Duration duration) {
        if (duration == null) return "00:00";
        return String.format("%02d",duration.toMinutesPart()) + ":" + String.format("%02d",duration.toSecondsPart());
    }
}
