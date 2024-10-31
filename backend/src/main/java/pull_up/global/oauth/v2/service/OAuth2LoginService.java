package pull_up.global.oauth.v2.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import pull_up.api.member.entity.Member;
import pull_up.api.member.repository.MemberRepository;
import pull_up.global.oauth.v2.dto.AppleLoginRequestDto;
import pull_up.global.oauth.v2.dto.KakaoUserInfoDto;
import pull_up.global.oauth.v2.dto.OAuth2LoginResponseDto;
import pull_up.global.oauth.v2.enums.OAuth2Provider;
import pull_up.global.oauth.v2.exception.OAuthError;
import pull_up.global.oauth.v2.exception.OAuthException;
import pull_up.global.oauth.v2.util.AppleTokenDecoder;

import java.util.Optional;

import static pull_up.global.oauth.v2.exception.OAuthError.NOT_REGISTERED_MEMBER_IN_DATABASE;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2LoginService {

    private final MemberRepository memberRepository;
    private final AppleTokenDecoder appleTokenDecoder;
    private final Gson gson = new Gson();

    public OAuth2LoginResponseDto getKakaoUser(DefaultOAuth2User user) {
        KakaoUserInfoDto.KakaoAccount dto = gson.fromJson(gson.toJson(user.getAttributes().get("kakao_account")), KakaoUserInfoDto.KakaoAccount.class);

        Optional<Member> member = memberRepository.findByEmailAndRole(dto.email(), OAuth2Provider.KAKAO.getRole());
        if (member.isPresent()) {
            return OAuth2LoginResponseDto.of(member.get(), false, OAuth2Provider.KAKAO);

        } else {
            Member newMember = registKakaoMember(dto);
            return OAuth2LoginResponseDto.of(newMember, true, OAuth2Provider.KAKAO);
        }
    }

    public OAuth2LoginResponseDto getAppleUser(String idToken, String userJson) {
        String email = (String) appleTokenDecoder.decode(idToken).getPayload().get("email");

        if (userJson.equals("ALREADY_REGISTERED_USER")) {
            Member member = memberRepository.findByEmailAndRole(email, OAuth2Provider.APPLE.getRole())
                    .orElseThrow(() -> new OAuthException(NOT_REGISTERED_MEMBER_IN_DATABASE));
            return OAuth2LoginResponseDto.of(member, false, OAuth2Provider.APPLE);

        } else {
            Member member = registAppleMember(email, userJson);
            return OAuth2LoginResponseDto.of(member, true, OAuth2Provider.APPLE);
        }
    }

    private Member registKakaoMember(KakaoUserInfoDto.KakaoAccount dto) {
        Member member = Member.of(dto.profile().nickname(), dto.email(), false, "kakao_user");
        memberRepository.save(member);

        return member;
    }

    private Member registAppleMember(String email, String userJson) {
        if (memberRepository.findByEmailAndRole(email, OAuth2Provider.APPLE.getRole()).isPresent())
            throw new OAuthException(OAuthError.ALREADY_REGISTERED_MEMBER_WITH_USER_JSON);

        AppleLoginRequestDto dto = gson.fromJson(userJson, AppleLoginRequestDto.class);

        Member member = Member.of(dto.name().firstName(), dto.name().lastName(), email, false, "apple_user");
        memberRepository.save(member);

        return member;
    }
}
