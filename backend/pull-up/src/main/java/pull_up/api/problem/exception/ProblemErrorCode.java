package pull_up.api.problem.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 회원관련 ErrorCode.
 */
@Getter
@AllArgsConstructor
public enum ProblemErrorCode {

    NOT_FOUND_PROBLEM(HttpStatus.BAD_REQUEST, "해당 문제가 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
