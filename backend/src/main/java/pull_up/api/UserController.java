package pull_up.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@Slf4j
@RestController
public class UserController {
  @GetMapping("/api/pull-up/oauth2/callback/kakao")
  public Map<String, Object> userKakao(@AuthenticationPrincipal OAuth2User principal) {
    log.info("principal  :  {}", principal);
    return Collections.singletonMap("name", principal.getAttribute("name"));
  }

  @PostMapping("/api/pull-up/oauth2/callback/apple")
  public Map<String, Object> userApple(@AuthenticationPrincipal OAuth2User principal) {
    log.info("principal  :  {}", principal);
    return Collections.singletonMap("name", principal.getAttribute("name"));
  }
}
