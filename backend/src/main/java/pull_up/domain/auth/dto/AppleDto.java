package pull_up.domain.auth.dto;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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

    public record TokenReq(
            String code,
            String client_id,
            String client_secret,
            String grant_type
    ) {

        public MultiValueMap<String, String> getFormData() {
            MultiValueMap<String, String> ret = new LinkedMultiValueMap<>();
            ret.add("code",code);
            ret.add("client_id",client_id);
            ret.add("client_secret",client_secret);
            ret.add("grant_type",grant_type);
            return ret;
        }
    }

    public record TokenRes(
            String access_token,
            String expires_in,
            String id_token,
            String refresh_token,
            String token_type,
            String error
    ) {

    }

    public record RevokeReq(
            String client_id,
            String client_secret,
            String token
    ) {
        public MultiValueMap<String, String> getFormData() {
            MultiValueMap<String, String> ret = new LinkedMultiValueMap<>();
            ret.add("client_id",client_id);
            ret.add("client_secret",client_secret);
            ret.add("token", token);
            return ret;
        }
    }
}
