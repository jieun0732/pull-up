package pull_up.domain.auth.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import pull_up.infra.database.entity.Member;
import pull_up.infra.database.repository.member.MemberRepository;
import pull_up.infra.api.auth.KakaoAuthRestApi;
import pull_up.api.auth.dto.AppleLoginRequestDto;
import pull_up.api.auth.dto.KakaoLoginRequestDto;
import pull_up.api.auth.dto.KakaoUserInfoDto;
import pull_up.api.auth.dto.OAuth2LoginResponseDto;
import pull_up.domain.auth.enums.OAuth2Provider;
import pull_up.global.exception.auth.AuthException;
import pull_up.global.security.util.AppleTokenDecoder;

import java.util.Optional;

import static pull_up.global.exception.auth.AuthError.NOT_REGISTERED_MEMBER_IN_DATABASE;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2LoginService {

    private final MemberRepository memberRepository;
    private final AppleTokenDecoder appleTokenDecoder;
    private final KakaoAuthRestApi kakaoAuthRestApi;
    private final Gson gson = new Gson();

    public OAuth2LoginResponseDto getKakaoUser(KakaoUserInfoDto.KakaoAccount dto) {
        Optional<Member> member = memberRepository.findByEmailAndRole(dto.email(), OAuth2Provider.KAKAO.getRole());
        if (member.isPresent()) {
            return OAuth2LoginResponseDto.of(member.get(), false, OAuth2Provider.KAKAO);

        } else {
            Member newMember = registKakaoMember(dto);
            return OAuth2LoginResponseDto.of(newMember, true, OAuth2Provider.KAKAO);
        }
    }

    public OAuth2LoginResponseDto getKakaoUser(DefaultOAuth2User user) {
        KakaoUserInfoDto.KakaoAccount dto = gson.fromJson(gson.toJson(user.getAttributes().get("kakao_account")), KakaoUserInfoDto.KakaoAccount.class);
        return getKakaoUser(dto);
    }

    public OAuth2LoginResponseDto getKakaoUser(String code) {
        KakaoUserInfoDto userInfo = kakaoAuthRestApi.getUserInfo(new KakaoLoginRequestDto(code));
        return getKakaoUser(userInfo.kakao_account());
    }

    private Member registKakaoMember(KakaoUserInfoDto.KakaoAccount dto) {
        Member member = Member.of(dto.profile().nickname(), dto.email(), false, "kakao_user");
        memberRepository.save(member);

        return member;
    }

    public OAuth2LoginResponseDto getAppleUser(String idToken, String userJson) {
        String email = (String) appleTokenDecoder.decode(idToken).get("email");

        if (userJson.equals("ALREADY_REGISTERED_USER")) {
            Member member = memberRepository.findByEmailAndRole(email, OAuth2Provider.APPLE.getRole())
                    .orElseThrow(() -> new AuthException(NOT_REGISTERED_MEMBER_IN_DATABASE));
            return OAuth2LoginResponseDto.of(member, false, OAuth2Provider.APPLE);

        } else {
            Member member = registAppleMember(email, userJson);
            return OAuth2LoginResponseDto.of(member, true, OAuth2Provider.APPLE);
        }
    }

    private Member registAppleMember(String email, String userJson) {
        return memberRepository.findByEmailAndRole(email, OAuth2Provider.APPLE.getRole()).orElseGet(() -> {
            AppleLoginRequestDto dto = gson.fromJson(userJson, AppleLoginRequestDto.class);

            Member newMember = Member.of(dto.name().firstName(), dto.name().lastName(), email, false, "apple_user");
            memberRepository.save(newMember);

            return newMember;
        });
    }
}
