package pull_up.global.Oauth.v2.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pull_up.api.member.entity.Member;
import pull_up.api.member.repository.MemberRepository;
import pull_up.global.Oauth.v2.dto.AppleLoginRequestDto;
import pull_up.global.Oauth.v2.dto.KakaoUserInfoDto;
import pull_up.global.Oauth.v2.dto.OAuth2LoginResponseDto;
import pull_up.global.Oauth.v2.enums.OAuthProvider;
import pull_up.global.Oauth.v2.exception.OAuthError;
import pull_up.global.Oauth.v2.exception.OAuthException;

import static pull_up.global.Oauth.v2.exception.OAuthError.NOT_REGISTERED_MEMBER_WITHOUT_LOGIN_FLAG;

@Service
@RequiredArgsConstructor
public class OAuth2LoginService {
    private final MemberRepository memberRepository;

    public OAuth2LoginResponseDto getKakaoUser(KakaoUserInfoDto dto) {
        Member member = memberRepository.findByEmailAndRole(dto.kakao_account().email(), OAuthProvider.APPLE.getRole())
                .orElse(registKakaoMember(dto));
        return OAuth2LoginResponseDto.getDtoWithProvider(member, OAuthProvider.KAKAO);
    }

    private Member registKakaoMember(KakaoUserInfoDto dto) {
        Member member = Member.of(dto.kakao_account().profile().nickname(), dto.kakao_account().email(), false, "kakao_user");
        memberRepository.save(member);
        return member;
    }

    public OAuth2LoginResponseDto getAppleUser(Jws<Claims> decodedToken, AppleLoginRequestDto dto) {
        Member member;
        String email = (String) decodedToken.getPayload().get("email");
        if (dto.isFirstLogin())
            member = registAppleMember(email, dto);
        else
            member = memberRepository.findByEmailAndRole(email, OAuthProvider.APPLE.getRole())
                    .orElseThrow(() -> new OAuthException(NOT_REGISTERED_MEMBER_WITHOUT_LOGIN_FLAG));

        return OAuth2LoginResponseDto.getDtoWithProvider(member, OAuthProvider.APPLE);
    }

    private Member registAppleMember(String email, AppleLoginRequestDto dto) {
        if (memberRepository.findByEmailAndRole(email, OAuthProvider.APPLE.getRole()).isPresent())
            throw new OAuthException(OAuthError.ALREADY_REGISTERED_MEMBER_WITH_LOGIN_FLAG);
        Member member = Member.of(dto.firstName(), dto.lastName(), email, false, "apple_user");
        memberRepository.save(member);
        return member;
    }
}
