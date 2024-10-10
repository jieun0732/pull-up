package pull_up.global.Oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import pull_up.api.member.entity.Member;
import pull_up.api.member.exception.MemberErrorCode;
import pull_up.api.member.exception.MemberException;
import pull_up.api.member.repository.MemberRepository;

/**
 * 소셜 로그인이 성공적으로 이루어졌다면 Token 을 발급하고 redirect. (2)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final MemberRepository memberRepository;
    private static final String URI = "http://43.203.236.62:8080/api/pull-up/login";

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException, ServletException {

        // memberId 가져오기
        Long memberId = getMemberId(authentication);
        log.info("memberId = " + memberId);
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER));

        String name = member.getName();
        String email = member.getEmail();

        // 멤버 정보 전달을 위한 redirect
        String redirectUrl = UriComponentsBuilder.fromUriString(URI)
            .queryParam("memberId", memberId)
//            .queryParam("name", name)
            .queryParam("email", email)
            .build().toUriString();
        log.info("redirectUrl = " + redirectUrl);

        response.sendRedirect(redirectUrl);
    }

    // 사용자 인증 정보에서 memberId 추출하는 메서드
    private Long getMemberId(Authentication authentication) {
        log.info("authentication = " + authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        log.info("authentication.Principle = " + authentication.getPrincipal());
        log.info("userDetails = " + userDetails);
        return userDetails.getMemberId();
    }
}
