package pull_up.global.Oauth.v2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OAuthError {
  REQUEST_CONVERT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Converted Entity is Null. Might occurred convert problem with OAuth Request to Request Entity."),
  PARSE_APPLE_PRIVATE_KEY_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Error has been occurred to get Apple Private Key.");

  private final HttpStatus httpStatus;
  private final String message;
}
