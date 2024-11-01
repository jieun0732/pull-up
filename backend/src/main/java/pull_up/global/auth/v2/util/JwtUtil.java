package pull_up.global.auth.v2.util;

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
import pull_up.api.member.entity.Member;
import pull_up.global.auth.v2.dto.JwtUserInfoDto;
import pull_up.global.auth.v2.dto.OAuth2LoginResponseDto;
import pull_up.global.auth.v2.exception.OAuthError;
import pull_up.global.auth.v2.exception.OAuthException;

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

    public String getAccessToken(Long id, String name, String email, String role) {
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

    public String getAccessToken(Member member) {
        return getAccessToken(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

    public String getAccessToken(OAuth2LoginResponseDto dto) {
        if (dto.provider().equalsIgnoreCase("apple"))
            return getAccessToken(dto.memberId(), dto.name(), dto.email(), "apple-user");
        else if (dto.provider().equalsIgnoreCase("kakao"))
            return getAccessToken(dto.memberId(), dto.name(), dto.email(), "kakao-user");

        throw new OAuthException(OAuthError.NOT_PROVIDED_OAUTH2_VENDOR_REQUEST);
    }

    public void validate(String token) {
        try {
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject()
                    .equals(subject);

        } catch (JwtException e) {
            throw new OAuthException(OAuthError.PARSE_JWT_ERROR);
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
