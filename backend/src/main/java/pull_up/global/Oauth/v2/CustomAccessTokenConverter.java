package pull_up.global.Oauth.v2;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.security.PrivateKey;
import java.util.Date;

import static io.jsonwebtoken.Jwts.SIG.ES256;
import static java.nio.charset.StandardCharsets.UTF_8;
import static pull_up.global.Oauth.v2.OAuthError.PARSE_APPLE_PRIVATE_KEY_ERROR;
import static pull_up.global.Oauth.v2.OAuthError.REQUEST_CONVERT_ERROR;
import static pull_up.global.Oauth.v2.OAuthProvider.APPLE;

@Slf4j
@Component
public class CustomAccessTokenConverter implements Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> {

  @Value("${apple.token.keyPath}")
  private String path;

  @Value("${apple.token.keyId}")
  private String keyId;

  @Value("${apple.token.teamId}")
  private String teamId;

  @Value("${apple.token.clientId}")
  private String clientId;

  @Value("${apple.token.baseUrl}")
  private String url;

  @Value("${apple.token.expire}")
  private Integer expire;

  public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> getTokenClient() {
    DefaultAuthorizationCodeTokenResponseClient tokenClient = new DefaultAuthorizationCodeTokenResponseClient();
    tokenClient.setRequestEntityConverter(this);
    return tokenClient;
  }

  @Override
  public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest source) {
    RequestEntity<?> entity = new OAuth2AuthorizationCodeGrantRequestEntityConverter().convert(source);
    if (entity == null) throw new OAuthException(REQUEST_CONVERT_ERROR);

    MultiValueMap<String, String> params = getParams(entity);

    setProviderLogic(source, params);

    return new RequestEntity<>(params, entity.getHeaders(), entity.getMethod(), entity.getUrl());
  }

  private void setProviderLogic(OAuth2AuthorizationCodeGrantRequest source, MultiValueMap<String, String> params) {
    if (source.getClientRegistration().getRegistrationId().contains(APPLE.getValue())) {
      params.set("client_secret", createAppleClientSecret());
    }
  }

  private MultiValueMap<String, String> getParams(RequestEntity<?> entity) {
    //noinspection unchecked
    MultiValueMap<String, String> body = (MultiValueMap<String, String>) entity.getBody();
    log.info("oauth2 request body params: {}", body);
    return body;
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
    ClassPathResource resource = new ClassPathResource(path);

   try (InputStream in = resource.getInputStream()) {
     PEMParser pemParser = new PEMParser(new StringReader(IOUtils.toString(in, UTF_8)));
     PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();

     return new JcaPEMKeyConverter().getPrivateKey(object);
   } catch (IOException e) {
     throw new OAuthException(PARSE_APPLE_PRIVATE_KEY_ERROR);
   }
  }
}
