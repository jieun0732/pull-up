package pull_up.domain.auth.dto;

public record KakaoUserInfoDto(
        String id,
        KakaoAccount kakao_account
) {
    public record KakaoAccount(
            String email,
            Boolean has_email,
            Boolean is_email_valid,
            Boolean is_email_verified,
            Profile profile
    ) {
        public record Profile(
                String nickname,
                Boolean is_default_nickname
        ) {
        }
    }
}
