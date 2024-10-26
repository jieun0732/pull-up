package pull_up.global.Oauth.v2.util;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import pull_up.global.Oauth.v2.exception.OAuthException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.security.PrivateKey;
import java.util.Date;

import static io.jsonwebtoken.Jwts.SIG.ES256;
import static java.nio.charset.StandardCharsets.UTF_8;
import static pull_up.global.Oauth.v2.exception.OAuthError.PARSE_APPLE_PUBLIC_KEY_ERROR;

@Slf4j
@Component
public class AppleClientSecretGenerator {

    @Value("${auth.login.apple.token.keyPath}")
    private String path;

    @Value("${auth.login.apple.token.keyId}")
    private String keyId;

    @Value("${auth.login.apple.token.teamId}")
    private String teamId;

    @Value("${auth.login.apple.token.clientId}")
    private String clientId;

    @Value("${auth.login.apple.token.baseUrl}")
    private String url;

    @Value("${auth.login.apple.token.expire}")
    private Integer expire;

    public String generate() {
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
            throw new OAuthException(PARSE_APPLE_PUBLIC_KEY_ERROR);
        }
    }
}
