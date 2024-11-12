package pull_up.api.auth.dto;

import lombok.Builder;
import pull_up.infra.database.entity.Member;
import pull_up.domain.auth.enums.OAuth2Provider;

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
                return new OAuth2LoginResponseDto(firstLogin, member.getId(), "apple", member.getName(), getPrivateEmail(member.getEmail()));
            }
            case KAKAO -> {
                return new OAuth2LoginResponseDto(firstLogin, member.getId(), "kakao", member.getName(), member.getEmail());
            }
            default -> throw new IllegalStateException("Unexpected value: " + oAuth2Provider);
        }
    }

    public static String getPrivateEmail(String email) {
        if (email.equals("CONCEALED_EMAIL")) return email;
        if (email.split("@")[1].contains("private")) return "CONCEALED_EMAIL";
        return email;
    }
}
