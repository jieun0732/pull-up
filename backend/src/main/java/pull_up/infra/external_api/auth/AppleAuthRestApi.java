package pull_up.infra.external_api.auth;

import com.nimbusds.jose.util.StandardCharset;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileUrlResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pull_up.domain.auth.dto.AppleDto;
import pull_up.domain.auth.exception.AuthError;
import pull_up.domain.auth.exception.AuthException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.security.PrivateKey;
import java.util.Date;
import java.util.List;

import static io.jsonwebtoken.Jwts.SIG.ES256;
import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Component
public class AppleAuthRestApi {

    @Value("${auth.apple.token.key-path}")
    private String path;

    @Value("${auth.apple.token.key-id}")
    private String keyId;

    @Value("${auth.apple.token.team-id}")
    private String teamId;

    @Value("${spring.security.oauth2.client.registration.apple.client-id}")
    private String clientId;

    @Value("${auth.apple.token.base-uri}")
    private String url;

    @Value("${auth.apple.token.expire}")
    private Integer expire;

    @Value("${auth.apple.token.base-uri}")
    private String baseUri;

    public AppleDto.Jwks getKeys() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAcceptCharset(List.of(StandardCharset.UTF_8));

        HttpEntity<Object> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(baseUri + "/auth/keys", HttpMethod.GET, entity,
                new ParameterizedTypeReference<AppleDto.Jwks>() {
                }).getBody();
    }

    public String getRefreshToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAcceptCharset(List.of(StandardCharset.UTF_8));
        AppleDto.TokenReq req = new AppleDto.TokenReq(code, clientId, createAppleClientSecret(), "authorization_code");

        HttpEntity<Object> entity = new HttpEntity<>(req.getFormData(), headers);
        AppleDto.TokenRes res = restTemplate.exchange(baseUri + "/auth/token", HttpMethod.POST, entity,
                new ParameterizedTypeReference<AppleDto.TokenRes>() {
                }).getBody();

        return res.refresh_token();
    }

    public void revoke(String refreshToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAcceptCharset(List.of(StandardCharset.UTF_8));

        AppleDto.RevokeReq req = new AppleDto.RevokeReq(clientId, createAppleClientSecret(), refreshToken);

        HttpEntity<Object> entity = new HttpEntity<>(req.getFormData(), headers);
        restTemplate.postForEntity(baseUri + "/auth/revoke", entity, String.class);
    }

    private String createAppleClientSecret() {
        Date now = new Date(System.currentTimeMillis());
        Date expireDate = new Date(System.currentTimeMillis() + expire);
        return Jwts.builder()
                .header().keyId(keyId)
                .and()
                .audience().add(url)
                .and()
                .subject(clientId)
                .issuer(teamId)
                .issuedAt(now)
                .expiration(expireDate)
                .signWith(getPrivateKey(), ES256)
                .compact();
    }

    private PrivateKey getPrivateKey() {
        try {
            FileUrlResource resource = new FileUrlResource(path);
            try (InputStream in = resource.getInputStream()) {
                PEMParser pemParser = new PEMParser(new StringReader(IOUtils.toString(in, UTF_8)));
                PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
                return new JcaPEMKeyConverter().getPrivateKey(object);
            } catch (IOException e) {
                throw new AuthException(AuthError.PARSE_APPLE_PUBLIC_KEY_ERROR);
            }
        } catch (MalformedURLException e) {
            log.error("Private Key not exist in path.");
            throw new AuthException(AuthError.PARSE_APPLE_PUBLIC_KEY_ERROR);
        }
    }
}
