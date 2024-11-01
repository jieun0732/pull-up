package pull_up.global.auth.v2.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OAuthException extends RuntimeException {
    private OAuthError error;
    private String message;

    public OAuthException(OAuthError error) {
        this.error = error;
        this.message = error.getMessage();
    }
}
