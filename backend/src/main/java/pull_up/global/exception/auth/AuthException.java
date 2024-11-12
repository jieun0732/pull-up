package pull_up.global.exception.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthException extends RuntimeException {
    private AuthError error;
    private String message;

    public AuthException(AuthError error) {
        this.error = error;
        this.message = error.getMessage();
    }
}
