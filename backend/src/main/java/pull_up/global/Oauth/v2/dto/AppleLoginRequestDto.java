package pull_up.global.Oauth.v2.dto;

public record AppleLoginRequestDto(
        String token,
        Boolean isFirstLogin,
        String firstName,
        String lastName
) {
}
