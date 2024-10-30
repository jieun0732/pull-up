package pull_up.global.oauth.v2.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OAuthError {
    REQUEST_CONVERT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Converted Entity is Null. Might occurred convert problem with OAuth Request to Request Entity."),
    PARSE_APPLE_PUBLIC_KEY_ERROR(HttpStatus.UNAUTHORIZED, "Error has been occurred to get Apple Public Key."),
    NOT_REGISTERED_MEMBER_IN_DATABASE(HttpStatus.BAD_REQUEST, "Not Registered Member, But IsFirstLogin Flag is false."),
    ALREADY_REGISTERED_MEMBER_WITH_USER_JSON(HttpStatus.BAD_REQUEST, "Already Registered Member, But IsFirstLogin Flag is true."),
    NOT_PROVIDED_OAUTH2_VENDOR_REQUEST(HttpStatus.BAD_REQUEST, "Not Provided Vendor Request."),
    APPLE_USER_PARSE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Apple User parse throws exception.");

    private final HttpStatus httpStatus;
    private final String message;
}
