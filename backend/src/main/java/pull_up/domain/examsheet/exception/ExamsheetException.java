package pull_up.domain.examsheet.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 회원 관련 Exception.
 */
@Getter
@AllArgsConstructor
public class ExamsheetException extends RuntimeException {

    private ExamsheetErrorCode errorCode;
    private String message;

    /**
     * 메세지가 없는 생성자.
     */
    public ExamsheetException(ExamsheetErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }
}
