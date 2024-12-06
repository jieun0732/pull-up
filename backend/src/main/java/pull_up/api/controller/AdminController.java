package pull_up.api.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AuthenticationManager authenticationManager;

    @GetMapping("/")
    public RedirectView redirect() {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/admin");
        return redirectView;
    }

    @GetMapping("/admin")
    public String adminMainPage() {
        return "index";
    }

    @GetMapping("/admin/login")
    public String loginPage() {
        return "login";
    }


    @PostMapping("/admin/login")
    public void login(@RequestParam String username, @RequestParam String password, HttpServletResponse response) throws IOException {
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
        authenticationManager.authenticate(authenticationRequest);
        response.sendRedirect("/admin");
    }

}
