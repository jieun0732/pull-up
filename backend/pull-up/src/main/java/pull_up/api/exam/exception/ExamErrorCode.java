package pull_up.api.exam.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 회원관련 ErrorCode.
 */
@Getter
@AllArgsConstructor
public enum ExamErrorCode {


    NOT_FOUND_EXAM(HttpStatus.BAD_REQUEST, "요청한 내용의 시험이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
