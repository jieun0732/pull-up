package pull_up.global.Oauth.v2;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
public class OAuth2SuccessHandlerV2  implements AuthenticationSuccessHandler {

  @Value("${auth.login.redirect-uri}")
  private String URI;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    log.info("authentication : {}", authentication);

    String redirectUrl = UriComponentsBuilder.fromUriString(URI)
      .queryParam("login", "true")
      .build().toUriString();
    response.sendRedirect(redirectUrl);
  }
}
