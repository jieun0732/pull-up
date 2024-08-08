package pull_up.global.Oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import pull_up.api.member.entity.Member;
import pull_up.api.member.repository.MemberRepository;

// 172.30.1.39
// http://localhost:8001/api/pull-up/oauth2/authorization/kakao
// http://localhost:8001/api/pull-up/oauth2/authorization/apple

@Service
@Slf4j
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    public OAuth2UserService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info(registrationId);
        
        // 어떤 로그인인지 구분
        Map<String, Object> attributes;
        if ("apple".equals(registrationId)) {
            log.info("Processing Apple OAuth2 login");
            String idToken = userRequest.getAdditionalParameters().get("id_token").toString();
            attributes = new HashMap<>(decodeJwtTokenPayload(idToken));
            attributes.put("id_token", idToken);
        } else {
            log.info("Processing OAuth2 login for provider: {}", registrationId);
            OAuth2User oAuth2User = super.loadUser(userRequest);
            attributes =  new HashMap<>(oAuth2User.getAttributes());
        }

        // 해석된 정보 Dto를 통해 필터링
        String oauthClientName = userRequest.getClientRegistration().getClientName();
        OAuth2UserInfoDto oAuth2UserInfoDTO = OAuth2UserInfoDto.of(oauthClientName, attributes);
        log.info("oAuth2UserInfoDTO = {}", oAuth2UserInfoDTO);

        Member member = getOrSave(oAuth2UserInfoDTO);
        log.info("member = {}", member);

        // attributes에 멤버 정보 추가
        attributes.put("member", member);

        // nameAttributeKey
        String userNameAttributeName = userRequest.getClientRegistration()
            .getProviderDetails()
            .getUserInfoEndpoint()
            .getUserNameAttributeName();

        log.info("principle " + new CustomUserDetails(member, attributes, userNameAttributeName));
        return new CustomUserDetails(member, attributes, userNameAttributeName);
    }

    //JWT Payload부분 decode 메서드
    public Map<String, Object> decodeJwtTokenPayload(String jwtToken){
        Map<String, Object> jwtClaims = new HashMap<>();
        try {
            String[] parts = jwtToken.split("\\.");
            Base64.Decoder decoder = Base64.getUrlDecoder();

            byte[] decodedBytes = decoder.decode(parts[1].getBytes(StandardCharsets.UTF_8));
            String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();

            Map<String, Object> map = mapper.readValue(decodedString, Map.class);
            jwtClaims.putAll(map);

        } catch (JsonProcessingException e) {
            log.error("decodeJwtToken: {}-{} / jwtToken : {}", e.getMessage(), e.getCause(), jwtToken);
        }
        return jwtClaims;
    }

    private Member getOrSave(OAuth2UserInfoDto oAuth2UserInfo) {
        Member member = memberRepository.findByEmail(oAuth2UserInfo.email())
            .orElseGet(oAuth2UserInfo::toEntity);
        return memberRepository.save(member);
    }
}