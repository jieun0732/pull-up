package pull_up.domain.examsheet.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExamsheetErrorCode {
    NOT_FOUND_EXAMSHEET(HttpStatus.NOT_FOUND, "요청한 내용의 시험지가 없습니다."),
    PROBLEM_NUMBER_EXCEED(HttpStatus.BAD_REQUEST, "요청한 문제 번호가 시험지의 문제 개수 범위를 초과합니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
