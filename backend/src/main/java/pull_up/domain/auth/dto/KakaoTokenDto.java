package pull_up.domain.auth.dto;

public record KakaoTokenDto(
        String token_type,
        String access_token,
        Integer expires_in,
        String refresh_token,
        Integer refrest_token_expires_in
) {
}
