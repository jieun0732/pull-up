package pull_up.domain.auth.dto;

import java.util.List;

public record AppleDto() {
    public record Jwks(
            List<Jwk> keys
    ) {
        public record Jwk(
                String kty, String kid, String use, String alg, String n, String e
        ) {
        }
    }
}
