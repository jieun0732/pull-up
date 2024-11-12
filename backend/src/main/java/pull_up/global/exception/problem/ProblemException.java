package pull_up.global.exception.problem;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
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
