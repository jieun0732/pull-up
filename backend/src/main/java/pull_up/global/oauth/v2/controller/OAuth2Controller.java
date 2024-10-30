package pull_up.global.oauth.v2.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pull_up.global.oauth.v2.dto.OAuth2LoginResponseDto;
import pull_up.global.oauth.v2.service.OAuth2LoginService;

import java.util.Map;
import java.util.Properties;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OAuth2Controller {

    @Value("${auth.apple.frontend-redirect-uri}")
    private String redirectUri;

    private final OAuth2LoginService oAuth2LoginService;

    @PostMapping("/api/pull-up/oauth2/callback/apple")
    RedirectView appleLogin(
            HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        String idToken = parameterMap.get("id_token")[0];
        String userJson = parameterMap.getOrDefault("user", new String[] {"ALREADY_REGISTERED_USER"})[0];

        return setRedirect(oAuth2LoginService.getAppleUser(idToken, userJson));
    }

    private RedirectView setRedirect(OAuth2LoginResponseDto appleUser) {
        Properties attributes = new Properties();
        attributes.setProperty("login", appleUser.login().toString());
        attributes.setProperty("memberId", appleUser.memberId().toString());
        attributes.setProperty("name", appleUser.name());
        attributes.setProperty("email", appleUser.email());
        attributes.setProperty("provider", appleUser.provider());

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(redirectUri);
        redirectView.setAttributes(attributes);
        return redirectView;
    }
}
