package pull_up.domain.auth.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import pull_up.infra.database.entity.legacy.MemberL;
import pull_up.infra.database.repository.member.MemberRepository;
import pull_up.infra.external_api.auth.KakaoAuthRestApi;
import pull_up.api.auth.dto.AppleLoginRequestDto;
import pull_up.api.auth.dto.KakaoLoginRequestDto;
import pull_up.api.auth.dto.KakaoUserInfoDto;
import pull_up.api.auth.dto.OAuth2LoginResponseDto;
import pull_up.domain.auth.SNSProvider;
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
        Optional<MemberL> member = memberRepository.findByEmailAndRole(dto.email(), SNSProvider.KAKAO.getRole());
        if (member.isPresent()) {
            return OAuth2LoginResponseDto.of(member.get(), false, SNSProvider.KAKAO);

        } else {
            MemberL newMemberL = registKakaoMember(dto);
            return OAuth2LoginResponseDto.of(newMemberL, true, SNSProvider.KAKAO);
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

    private MemberL registKakaoMember(KakaoUserInfoDto.KakaoAccount dto) {
        MemberL memberL = MemberL.of(dto.profile().nickname(), dto.email(), false, "kakao_user");
        memberRepository.save(memberL);

        return memberL;
    }

    public OAuth2LoginResponseDto getAppleUser(String idToken, String userJson) {
        String email = (String) appleTokenDecoder.decode(idToken).get("email");

        if (userJson.equals("ALREADY_REGISTERED_USER")) {
            MemberL memberL = memberRepository.findByEmailAndRole(email, SNSProvider.APPLE.getRole())
                    .orElseThrow(() -> new AuthException(NOT_REGISTERED_MEMBER_IN_DATABASE));
            return OAuth2LoginResponseDto.of(memberL, false, SNSProvider.APPLE);

        } else {
            MemberL memberL = registAppleMember(email, userJson);
            return OAuth2LoginResponseDto.of(memberL, true, SNSProvider.APPLE);
        }
    }

    private MemberL registAppleMember(String email, String userJson) {
        return memberRepository.findByEmailAndRole(email, SNSProvider.APPLE.getRole()).orElseGet(() -> {
            AppleLoginRequestDto dto = gson.fromJson(userJson, AppleLoginRequestDto.class);

            MemberL newMemberL = MemberL.of(dto.name().firstName(), dto.name().lastName(), email, false, "apple_user");
            memberRepository.save(newMemberL);

            return newMemberL;
        });
    }
}
