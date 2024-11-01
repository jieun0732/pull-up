package pull_up.global.auth.v2.dto;

import java.util.HashMap;
import java.util.Map;

public record JwtUserInfoDto(
        Long id, String name, String email, String role
) {
    public static Map<String, Object> getAttributes(JwtUserInfoDto userInfo) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", userInfo.id);
        attributes.put("name", userInfo.name);
        attributes.put("email", userInfo.email);
        attributes.put("role", userInfo.role);

        return attributes;
    }
}
