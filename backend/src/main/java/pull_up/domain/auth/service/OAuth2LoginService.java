package pull_up.domain.auth.service;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import pull_up.domain.auth.SNSProvider;
import pull_up.domain.auth.dto.KakaoDto;
import pull_up.domain.auth.dto.OAuth2Login;
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

    public OAuth2Login.Response getKakaoUser(KakaoDto.KakaoUserInfo dto) {
        Optional<Member> member = memberRepository.findBySnsId(dto.id());

        return member.map(value -> OAuth2Login.Response.toDto(value, false))
                .orElseGet(() -> OAuth2Login.Response.toDto(registKakaoMember(dto), true));
    }

    public OAuth2Login.Response getKakaoUser(DefaultOAuth2User user) {
        KakaoDto.KakaoUserInfo dto = gson.fromJson(gson.toJson(user.getAttributes()), KakaoDto.KakaoUserInfo.class);
        return getKakaoUser(dto);
    }

    public OAuth2Login.Response getKakaoUser(String code) {
        KakaoDto.KakaoUserInfo userInfo = kakaoAuthRestApi.getUserInfo(new OAuth2Login.Request.Kakao(code));
        return getKakaoUser(userInfo);
    }

    public OAuth2Login.Response getAppleUser(String idToken, String userJson) {
        Claims decodedToken = appleTokenDecoder.decode(idToken);
        String id = (String) decodedToken.get("sub");

        Optional<Member> member = memberRepository.findBySnsId(id);

        return member.map(value -> OAuth2Login.Response.toDto(value, false))
                .orElseGet(() -> OAuth2Login.Response.toDto(registAppleMember(id, userJson), true));
    }

    public OAuth2Login.Response getLocalUser() {
        return new OAuth2Login.Response(true,99999999L, "local", "test user", "test@example.com");
    }

    private Member registKakaoMember(KakaoDto.KakaoUserInfo dto) {

        Member firstLoginMember = Member.getFirstLoginMember(dto.kakao_account().profile().nickname(), dto.kakao_account().email(), dto.id(), KAKAO);
        memberRepository.save(firstLoginMember);

        return firstLoginMember;
    }

    private Member registAppleMember(String id, String userJson) {
        OAuth2Login.Request.Apple dto = gson.fromJson(userJson, OAuth2Login.Request.Apple.class);
        Member firstLoginMember = Member.getFirstLoginMember(dto.name().firstName(), dto.name().lastName(), dto.email(), id, APPLE);
        memberRepository.save(firstLoginMember);

        return firstLoginMember;
    }
}
