package pull_up.global.exception.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 회원 관련 Exception.
 */
@Getter
@AllArgsConstructor
public class MemberAnswerException extends RuntimeException {

    private MemberAnswerErrorCode errorCode;
    private String message;

    /**
     * 메세지가 없는 생성자.
     */
    public MemberAnswerException(MemberAnswerErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }
}
