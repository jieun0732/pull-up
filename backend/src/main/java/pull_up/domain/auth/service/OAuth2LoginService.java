package pull_up.domain.auth.service;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import pull_up.domain.auth.dto.AppleLoginRequestDto;
import pull_up.domain.auth.dto.KakaoLoginRequestDto;
import pull_up.domain.auth.dto.KakaoUserInfoDto;
import pull_up.domain.auth.dto.OAuth2LoginResponseDto;
import pull_up.domain.auth.SNSProvider;
import pull_up.domain.dao.MemberRepository;
import pull_up.global.security.util.AppleTokenDecoder;
import pull_up.infra.database.jpa.entity.Member;
import pull_up.infra.external_api.auth.KakaoAuthRestApi;

import java.util.Optional;

import static pull_up.domain.auth.SNSProvider.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2LoginService {

    private final MemberRepository memberRepository;
    private final AppleTokenDecoder appleTokenDecoder;
    private final KakaoAuthRestApi kakaoAuthRestApi;
    private final Gson gson = new Gson();

    public OAuth2LoginResponseDto getKakaoUser(KakaoUserInfoDto dto) {
        Optional<Member> member = memberRepository.findBySnsId(dto.id());

        return member.map(value -> OAuth2LoginResponseDto.of(value, false, KAKAO))
                .orElseGet(() -> OAuth2LoginResponseDto.of(registKakaoMember(dto), true, KAKAO));
    }

    public OAuth2LoginResponseDto getKakaoUser(DefaultOAuth2User user) {
        KakaoUserInfoDto dto = gson.fromJson(gson.toJson(user.getAttributes()), KakaoUserInfoDto.class);
        return getKakaoUser(dto);
    }

    public OAuth2LoginResponseDto getKakaoUser(String code) {
        KakaoUserInfoDto userInfo = kakaoAuthRestApi.getUserInfo(new KakaoLoginRequestDto(code));
        return getKakaoUser(userInfo);
    }

    private Member registKakaoMember(KakaoUserInfoDto dto) {

        Member firstLoginMember = Member.getFirstLoginMember(dto.kakao_account().profile().nickname(), dto.kakao_account().email(), dto.id(), KAKAO);
        memberRepository.save(firstLoginMember);

        return firstLoginMember;
    }

    public OAuth2LoginResponseDto getAppleUser(String idToken, String userJson) {
        Claims decodedToken = appleTokenDecoder.decode(idToken);
        String id = (String) decodedToken.get("sub");

        Optional<Member> member = memberRepository.findBySnsId(id);

        return member.map(value -> OAuth2LoginResponseDto.of(value, false, SNSProvider.APPLE))
                .orElseGet(() -> OAuth2LoginResponseDto.of(registAppleMember(id, userJson), true, APPLE));
    }

    private Member registAppleMember(String id, String userJson) {
        AppleLoginRequestDto dto = gson.fromJson(userJson, AppleLoginRequestDto.class);
        Member firstLoginMember = Member.getFirstLoginMember(dto.name().firstName(), dto.name().lastName(), dto.email(), id, APPLE);
        memberRepository.save(firstLoginMember);

        return firstLoginMember;
    }
}
