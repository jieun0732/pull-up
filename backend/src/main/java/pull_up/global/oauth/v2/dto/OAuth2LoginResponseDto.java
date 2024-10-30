package pull_up.global.oauth.v2.dto;

import pull_up.api.member.entity.Member;
import pull_up.global.oauth.v2.enums.OAuth2Provider;

public record OAuth2LoginResponseDto(
        Boolean login,
        Long memberId,
        String provider,
        String name,
        String email
) {

    public static OAuth2LoginResponseDto getDtoWithProvider(Member member, OAuth2Provider oAuth2Provider) {
        switch (oAuth2Provider) {
            case APPLE -> {
                return new OAuth2LoginResponseDto(true, member.getId(), "apple", member.getName(), member.getEmail());
            }
            case KAKAO -> {
                return new OAuth2LoginResponseDto(true, member.getId(), "kakao", member.getName(), member.getEmail());
            }
            default -> throw new IllegalStateException("Unexpected value: " + oAuth2Provider);
        }
    }
}
