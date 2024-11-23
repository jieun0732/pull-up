package pull_up.domain.auth.dto;

public record KakaoDto() {

    public record KakaoUserInfo(
            String id,
            KakaoAccount kakao_account
    ) {
        public record KakaoAccount(
                String email,
                Boolean has_email,
                Boolean is_email_valid,
                Boolean is_email_verified,
                KakaoAccount.Profile profile
        ) {
            public record Profile(
                    String nickname,
                    Boolean is_default_nickname
            ) {
            }
        }
    }

    public record KakaoTokenDto(
            String token_type,
            String access_token,
            Integer expires_in,
            String refresh_token,
            Integer refresh_token_expires_in
    ) {
    }
}
