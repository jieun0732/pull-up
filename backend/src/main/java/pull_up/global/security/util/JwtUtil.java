package pull_up.global.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;
import pull_up.domain.auth.Role;
import pull_up.domain.auth.dto.JwtUserInfoDto;
import pull_up.domain.auth.dto.OAuth2Login;
import pull_up.domain.auth.exception.AuthError;
import pull_up.domain.auth.exception.AuthException;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    @Value("${auth.jwt.key}")
    private String key;

    @Value("${auth.jwt.subject}")
    private String subject;

    @Value("${auth.jwt.issuer}")
    private String issuer;

    @Value("${auth.jwt.audience}")
    private String audience;

    @Value("${auth.jwt.expire}")
    private Long expire;

    public String getAccessToken(Long id, String name, String email, Role role) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expire);

        return Jwts.builder()

                .id(id.toString())
                .subject(subject)
                .issuer(issuer)
                .audience().add(audience).and()
                .issuedAt(now)
                .expiration(exp)
                .claim("name", name)
                .claim("email", email)
                .claim("role", role)

                .signWith(Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    public String getAccessToken(OAuth2Login.Response dto) {
        if (dto.provider().equalsIgnoreCase("apple"))
            return getAccessToken(dto.memberId(), dto.name(), dto.email(), Role.USER);
        else if (dto.provider().equalsIgnoreCase("kakao"))
            return getAccessToken(dto.memberId(), dto.name(), dto.email(), Role.USER);
        else if (dto.provider().equalsIgnoreCase("local"))
            return getAccessToken(dto.memberId(), dto.name(), dto.email(), Role.USER);

        throw new AuthException(AuthError.NOT_PROVIDED_OAUTH2_VENDOR_REQUEST);
    }

    public void validate(String token) {
        try {
            if (Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject()
                    .equals(subject)) return;

            throw new AuthException(AuthError.PARSE_JWT_ERROR);
        } catch (JwtException e) {
            System.out.println("e = " + e);
            throw new AuthException(AuthError.PARSE_JWT_ERROR);
        }
    }

    public JwtUserInfoDto getUserInfo(String accessToken) {
        validate(accessToken);
        Claims payload = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(accessToken).getPayload();

        return new JwtUserInfoDto(
                Long.valueOf(payload.getId()),
                (String) payload.get("name"),
                (String) payload.get("email"),
                (String) payload.get("role"));
    }

    public Authentication getAuthentication(String accessToken) {
        JwtUserInfoDto userInfo = getUserInfo(accessToken);
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userInfo.role());
        DefaultOAuth2User user = new DefaultOAuth2User(List.of(authority), JwtUserInfoDto.getAttributes(userInfo), "id");

        return new UsernamePasswordAuthenticationToken(user, accessToken, List.of(authority));
    }
}
