package pull_up.global.security.util;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pull_up.domain.auth.dto.AppleDto;
import pull_up.infra.external_api.auth.AppleAuthRestApi;
import pull_up.domain.auth.exception.AuthError;
import pull_up.domain.auth.exception.AuthException;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppleTokenDecoder {

    private final AppleAuthRestApi appleAuthRestApi;

    @Value("${spring.security.oauth2.client.registration.apple.client-id}")
    private String clientId;

    @Value("${auth.apple.token.base-uri}")
    private String baseUri;

    public Claims decode(String token) {
        AppleDto.Jwks keys = appleAuthRestApi.getKeys();
        log.info("token : {}", token);
        return Jwts.parser()
                .keyLocator(new AppleKeyLocator(keys))
                .requireAudience(clientId)
                .requireIssuer(baseUri)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public class AppleKeyLocator extends LocatorAdapter<Key> {

        private final AppleDto.Jwks appleJwks;

        public AppleKeyLocator(AppleDto.Jwks appleJwks) {
            this.appleJwks = appleJwks;
        }

        @Override
        public Key locate(ProtectedHeader header) {
            String publicKeyId = header.getKeyId();
            String algorithm = header.getAlgorithm();

            // 서명된 keyId, algorithm 으로부터 공개키 찾기
            try {
                AppleDto.Jwks.Jwk publicKey = appleJwks.keys().stream().filter(
                                key -> key.kid().equals(publicKeyId) && key.alg().equals(algorithm))
                        .findFirst()
                        .orElseThrow(() -> new AuthException(AuthError.PARSE_APPLE_PUBLIC_KEY_ERROR));

                byte[] nBytes = Base64.getUrlDecoder().decode(publicKey.n());
                byte[] eBytes = Base64.getUrlDecoder().decode(publicKey.e());

                BigInteger n = new BigInteger(1, nBytes);
                BigInteger e = new BigInteger(1, eBytes);

                RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
                KeyFactory keyFactory = KeyFactory.getInstance(publicKey.kty());

                return keyFactory.generatePublic(publicKeySpec);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                throw new AuthException(AuthError.PARSE_APPLE_PUBLIC_KEY_ERROR);
            }
        }
    }
}
