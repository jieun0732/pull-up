package pull_up.domain.auth.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pull_up.domain.auth.dto.KakaoDto;
import pull_up.domain.auth.dto.OAuth2Login;
import pull_up.domain.dao.MemberRepository;
import pull_up.domain.member.exception.MemberErrorCode;
import pull_up.domain.member.exception.MemberException;
import pull_up.global.security.util.AppleTokenDecoder;
import pull_up.infra.database.jpa.entity.Member;
import pull_up.infra.external_api.auth.AppleAuthRestApi;
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
    private final AppleAuthRestApi appleAuthRestApi;
    private final Gson gson = new Gson();

    @Transactional
    public OAuth2Login.Response getKakaoUser(KakaoDto.KakaoUserInfo dto) {
        Optional<Member> member = memberRepository.findBySnsId(dto.id());

        return member.map(value -> OAuth2Login.Response.toDto(value, false))
                .orElseGet(() -> OAuth2Login.Response.toDto(registKakaoMember(dto), true));
    }

    @Transactional
    public OAuth2Login.Response getKakaoUser(DefaultOAuth2User user) {
        KakaoDto.KakaoUserInfo dto = gson.fromJson(gson.toJson(user.getAttributes()), KakaoDto.KakaoUserInfo.class);
        return getKakaoUser(dto);
    }

    @Transactional
    public OAuth2Login.Response getKakaoUser(String code) {
        KakaoDto.KakaoUserInfo userInfo = kakaoAuthRestApi.getUserInfo(new OAuth2Login.Request.Kakao(code));
        return getKakaoUser(userInfo);
    }

    @Transactional
    public OAuth2Login.Response getAppleUser(String idToken, String code, String userJson) {
        String snsId = (String) appleTokenDecoder.decode(idToken).get("sub");
        Optional<Member> member = memberRepository.findBySnsId(snsId);

        return member.map(value -> OAuth2Login.Response.toDto(value, false))
                .orElseGet(() -> OAuth2Login.Response.toDto(registAppleMember(snsId, code, userJson), true));
    }

    public OAuth2Login.Response getLocalUser() {
        return new OAuth2Login.Response(true,99999999L, "local", "test user", "test@example.com");
    }

    public OAuth2Login.Response getLocalUser(String snsId) {
        Member member = memberRepository.findBySnsId(snsId).orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER));
        return OAuth2Login.Response.toDto(member,false);
    }

    @Transactional
    public OAuth2Login.Response getLocalUser(OAuth2Login.Request.Local request) {
        memberRepository.findBySnsId(request.snsId()).ifPresent(member -> {throw new MemberException(MemberErrorCode.DUPLICATED_SNS_ID);});
        Member firstLoginMember = Member.getFirstLoginMember(request.name(), request.email(), request.snsId(), LOCAL);

        memberRepository.save(firstLoginMember);

        return OAuth2Login.Response.toDto(firstLoginMember, true);
    }

    private Member registKakaoMember(KakaoDto.KakaoUserInfo dto) {

        Member firstLoginMember = Member.getFirstLoginMember(dto.kakao_account().profile().nickname(), dto.kakao_account().email(), dto.id(), KAKAO);
        memberRepository.save(firstLoginMember);

        return firstLoginMember;
    }

    private Member registAppleMember(String snsId, String code, String userJson) {
        OAuth2Login.Request.Apple dto = gson.fromJson(userJson, OAuth2Login.Request.Apple.class);
        String refreshToken = appleAuthRestApi.getRefreshToken(code);
        log.info("Apple Refresh Token : {}", refreshToken);

        Member firstLoginMember = Member.getFirstLoginMember(dto.name().firstName(), dto.name().lastName(), dto.email(), snsId, APPLE);
        firstLoginMember.setRefreshToken(refreshToken);
        memberRepository.save(firstLoginMember);

        return firstLoginMember;
    }
}
