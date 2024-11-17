package pull_up.domain.member.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
