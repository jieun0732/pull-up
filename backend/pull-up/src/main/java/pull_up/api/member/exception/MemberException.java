package pull_up.api.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 회원 관련 Exception.
 */
@Getter
@AllArgsConstructor
public class MemberException extends RuntimeException {

    private MemberErrorCode errorCode;
    private String message;

    /**
     * 메세지가 없는 생성자.
     */
    public MemberException(MemberErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }
}
