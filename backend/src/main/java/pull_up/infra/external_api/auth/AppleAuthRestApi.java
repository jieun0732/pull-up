package pull_up.infra.external_api.auth;

import com.nimbusds.jose.util.StandardCharset;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pull_up.domain.auth.dto.AppleJwks;

import java.util.List;

@Slf4j
@Component
public class AppleAuthRestApi {

    @Value("${spring.security.oauth2.client.provider.apple.jwk-set-uri}")
    private String jwkSetBaseUri;

    public AppleJwks getKeys() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAcceptCharset(List.of(StandardCharset.UTF_8));

        HttpEntity<Object> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(jwkSetBaseUri, HttpMethod.GET, entity,
                new ParameterizedTypeReference<AppleJwks>() {
                }).getBody();
    }
}
