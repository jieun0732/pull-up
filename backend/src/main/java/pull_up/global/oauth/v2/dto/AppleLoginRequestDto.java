package pull_up.global.oauth.v2.dto;

public record AppleLoginRequestDto(
        UserName name,
        String email
) {
    public record UserName(
            String firstName,
            String lastName
    ) {}
}
