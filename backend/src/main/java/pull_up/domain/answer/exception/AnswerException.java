package pull_up.domain.answer.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 회원 관련 Exception.
 */
@Getter
@AllArgsConstructor
public class AnswerException extends RuntimeException {

    private AnswerErrorCode errorCode;
    private String message;

    /**
     * 메세지가 없는 생성자.
     */
    public AnswerException(AnswerErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }
}
