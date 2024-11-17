package pull_up.domain.auth.dto;

public record AppleLoginRequestDto(
        UserName name,
        String email
) {
    public record UserName(
            String firstName,
            String lastName
    ) {}
}
