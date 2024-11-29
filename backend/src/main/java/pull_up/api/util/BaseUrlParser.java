package pull_up.api.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.util.UriComponentsBuilder;

public class BaseUrlParser {

    public static String getBaseUrlWithQueryString(HttpServletRequest request) {
        return UriComponentsBuilder.fromUriString(request.getRequestURI() + "?" + request.getQueryString())
                .replaceQueryParam("page")
                .replaceQueryParam("size")
                .build().toString();
    }

}
