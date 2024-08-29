package pull_up.api.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 회원관련 ErrorCode.
 */
@Getter
@AllArgsConstructor
public enum MemberAnswerErrorCode {

    NOT_FOUND_MEMBERANSWER(HttpStatus.NOT_FOUND, "사용자의 문제를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
