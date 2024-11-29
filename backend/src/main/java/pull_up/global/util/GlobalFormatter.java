package pull_up.global.util;

import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
public class GlobalFormatter {

    public static final DateTimeFormatter KOREAN_DATE_FORMATTER = DateTimeFormatter.ofPattern("MM월 dd일");

}
