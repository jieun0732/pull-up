package pull_up.infra.external_api.auth;

import com.nimbusds.jose.util.StandardCharset;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import pull_up.domain.auth.dto.KakaoLoginRequestDto;
import pull_up.domain.auth.dto.KakaoTokenDto;
import pull_up.domain.auth.dto.KakaoUserInfoDto;

import java.util.List;

@Slf4j
@Component
public class KakaoAuthRestApi {

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String tokenBaseUri;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String userInfoBaseUri;

    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
    private String grantType;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    public KakaoUserInfoDto getUserInfo(KakaoLoginRequestDto request) {
        String token = getToken(request.code());
        return getUserInfo(token);
    }

    private String getToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAcceptCharset(List.of(StandardCharset.UTF_8));

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", grantType);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        HttpEntity<Object> entity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(tokenBaseUri, HttpMethod.POST, entity, KakaoTokenDto.class).getBody().access_token();
    }

    private KakaoUserInfoDto getUserInfo(String token) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAcceptCharset(List.of(StandardCharset.UTF_8));

        HttpEntity<Object> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(userInfoBaseUri, HttpMethod.GET, entity, KakaoUserInfoDto.class).getBody();
    }
}
