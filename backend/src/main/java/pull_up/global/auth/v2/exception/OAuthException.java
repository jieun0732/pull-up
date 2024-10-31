package pull_up.global.auth.v2.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OAuthException extends RuntimeException {
    private OAuthError errorCode;
    private String message;

    public OAuthException(OAuthError errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }
}
