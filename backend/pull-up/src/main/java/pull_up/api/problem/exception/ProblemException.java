package pull_up.api.problem.exception;

import pull_up.api.member.exception.MemberErrorCode;

public class ProblemException extends RuntimeException {

    private ProblemErrorCode errorCode;
    private String message;

    /**
     * 메세지가 없는 생성자.
     */
    public ProblemException(ProblemErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }
}
