package pull_up.global.Oauth.v2.dto;

import pull_up.api.member.entity.Member;
import pull_up.global.Oauth.v2.enums.OAuthProvider;

public record OAuth2LoginResponseDto(
        Boolean login,
        Long memberId,
        String provider,
        String name,
        String email
) {

    public static OAuth2LoginResponseDto getDtoWithProvider(Member member, OAuthProvider oAuthProvider) {
        switch (oAuthProvider) {
            case APPLE -> {
                return new OAuth2LoginResponseDto(true, member.getId(), "apple", member.getName(), member.getEmail());
            }
            case KAKAO -> {
                return new OAuth2LoginResponseDto(true, member.getId(), "kakao", member.getName(), member.getEmail());
            }
            default -> throw new IllegalStateException("Unexpected value: " + oAuthProvider);
        }
    }
}
