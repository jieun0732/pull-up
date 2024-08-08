package pull_up.global.Oauth;

import java.util.Map;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import pull_up.api.member.entity.Member;
import pull_up.api.member.exception.MemberErrorCode;
import pull_up.api.member.exception.MemberException;

@Slf4j
@Builder
public record OAuth2UserInfoDto(String name, String email) {

    public static OAuth2UserInfoDto of(String oauthClientName, Map<String, Object> attributes) {
        return switch (oauthClientName) {
            case "naver" -> ofNaver(attributes);
            case "kakao" -> ofKakao(attributes);
            case "apple" -> ofApple(attributes);
            default -> throw new MemberException(MemberErrorCode.ILLEGAL_OAUTH2CLIENT_NAME);
        };
    }

    private static OAuth2UserInfoDto ofNaver(Map<String, Object> attributes) {
        log.info("Processing Naver login");
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
//        LocalDate birthday = LocalDate.parse(response.get("birthyear") + "-" + response.get("birthday"));
        return OAuth2UserInfoDto.builder()
            .name((String) response.get("name"))
            .email((String) response.get("email"))
            .build();
    }

    private static OAuth2UserInfoDto ofKakao(Map<String, Object> attributes) {
        log.info("Processing Kakao login");
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");
        return OAuth2UserInfoDto.builder()
            .name((String) profile.get("nickname"))
            .email((String) account.get("email"))
            .build();
    }

    private static OAuth2UserInfoDto ofApple(Map<String, Object> attributes) {
        log.info("Processing Apple login");
        return OAuth2UserInfoDto.builder()
            .name((String) attributes.get("name"))
            .email((String) attributes.get("email"))
            .build();
    }

    public Member toEntity() {
        return Member.of(name, email, false);
    }
}