package pull_up.api.exam.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 회원 관련 Exception.
 */
@Getter
@AllArgsConstructor
public class ExamException extends RuntimeException {

    private ExamErrorCode errorCode;
    private String message;

    /**
     * 메세지가 없는 생성자.
     */
    public ExamException(ExamErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }
}
