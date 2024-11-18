package pull_up.domain.auth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthError {
    REQUEST_CONVERT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Converted Entity is Null. Might occurred convert problem with OAuth Request to Request Entity."),
    PARSE_APPLE_PUBLIC_KEY_ERROR(HttpStatus.UNAUTHORIZED, "Error has been occurred to get Apple Public Key."),
    NOT_REGISTERED_MEMBER_IN_DATABASE(HttpStatus.BAD_REQUEST, "Not Registered Member, But Apple response doesn't have User json."),
    NOT_PROVIDED_OAUTH2_VENDOR_REQUEST(HttpStatus.BAD_REQUEST, "Not Provided Vendor Request."),
    APPLE_USER_PARSE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Apple User parse throws exception."),
    PARSE_JWT_ERROR(HttpStatus.UNAUTHORIZED, "Invalid Login Token."),
    NO_ACCESS_TOKEN_IN_COOKIES(HttpStatus.UNAUTHORIZED, "No Access Token in Cookies"),
    INTERNAL_SERVER_ERROR_WHILE_RESPONSE(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error while create response");

    private final HttpStatus httpStatus;
    private final String message;
}
