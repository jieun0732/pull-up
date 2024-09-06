package pull_up.api.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pull_up.api.member.entity.IncorrectAnswer;

@Getter
@AllArgsConstructor
public class IncorrectAnswerException extends RuntimeException {

    private IncorrectAnswerErrorCode errorCode;
    private String message;

    /**
     * 메세지가 없는 생성자.
     */
    public IncorrectAnswerException(IncorrectAnswerErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }
}
