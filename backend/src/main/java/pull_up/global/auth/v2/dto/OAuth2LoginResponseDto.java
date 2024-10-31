package pull_up.global.auth.v2.dto;

import lombok.Builder;
import pull_up.api.member.entity.Member;
import pull_up.global.auth.v2.enums.OAuth2Provider;

@Builder
public record OAuth2LoginResponseDto(
        Boolean firstLogin,
        Long memberId,
        String provider,
        String name,
        String email
) {

    public static OAuth2LoginResponseDto of(Member member, Boolean firstLogin, OAuth2Provider oAuth2Provider) {
        switch (oAuth2Provider) {
            case APPLE -> {
                return new OAuth2LoginResponseDto(firstLogin, member.getId(), "apple", member.getName(), member.getEmail());
            }
            case KAKAO -> {
                return new OAuth2LoginResponseDto(firstLogin, member.getId(), "kakao", member.getName(), member.getEmail());
            }
            default -> throw new IllegalStateException("Unexpected value: " + oAuth2Provider);
        }
    }
}
