package pull_up.global.Oauth.v2.dto;

import java.util.List;

public record AppleJwks(
        List<Jwk> keys
) {
    public record Jwk(
            String kty, String kid, String use, String alg, String n, String e
    ) {
    }
}
