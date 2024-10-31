package pull_up.global.auth.v2.dto;

public record JwtUserInfoDto(
        Long id, String name, String email, String role
) {
}
