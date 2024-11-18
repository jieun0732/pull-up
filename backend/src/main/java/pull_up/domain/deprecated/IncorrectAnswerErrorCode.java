package pull_up.domain.deprecated;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum IncorrectAnswerErrorCode {
    NOT_FOUND_INCORRECT_ANSWER(HttpStatus.BAD_REQUEST, "틀린 문제를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
