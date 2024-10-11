package pull_up.global.Oauth.apple;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppleController {
    @GetMapping("/oauth2/callback/apple")
    public String getAppleLoginUserInfo(HttpServletRequest request) {
        System.out.println("req.code" + request.getParameter("code"));
        System.out.println("req.user" + request.getParameter("user"));
        return request.getParameter("user") + " ||||| " + request.getParameter("code");
    }
}
