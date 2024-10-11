package pull_up.global.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pull_up.global.Oauth.AppleProperties;
import pull_up.global.Oauth.OAuth2SuccessHandler;
import pull_up.global.Oauth.OAuth2UserService;
import pull_up.global.Oauth.CustomRequestEntityConverter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final AppleProperties appleProperties;
    @Bean
    public CustomRequestEntityConverter customRequestEntityConverter() {
        return new CustomRequestEntityConverter(appleProperties);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // cors 허용
            .cors(cors -> cors
                .configurationSource(corsConfigurationSource())
            )
            // csrf 토큰 방어 해제.
            .csrf(CsrfConfigurer::disable)
            // httpBasic 인증 방식 해제.
            .httpBasic(HttpBasicConfigurer::disable)
            // 서버를 Stateless 하게 유지.
            .sessionManagement(sessionManagement -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            )
            .formLogin(AbstractHttpConfigurer::disable)

            // 카카오 로그인 추가
            .oauth2Login(oauth2 -> oauth2
                .tokenEndpoint(tokenEndpointConfig -> tokenEndpointConfig.accessTokenResponseClient(accessTokenResponseClient(customRequestEntityConverter())))
                .redirectionEndpoint(endpoint -> endpoint.baseUri("/oauth2/callback/*"))
                .userInfoEndpoint(endpoint -> endpoint.userService(oAuth2UserService))
                .successHandler(oAuth2SuccessHandler)
            )

            .authorizeHttpRequests(request -> request
                .requestMatchers("/api/pull-up/oauth2/**", "/api/pull-up/login")
                .permitAll()
                .requestMatchers("/api/pull-up/swagger-ui/**").permitAll()
                .requestMatchers("/api/pull-up/swagger-ui/index.html").permitAll()
                .requestMatchers("/api/pull-up/lawsuit/**").permitAll()
                .requestMatchers("/api/pull-up/").permitAll()
                .anyRequest().permitAll()
            );

//            // 인증 예외 처리
//            .exceptionHandling(exceptionHandling -> exceptionHandling
//                .authenticationEntryPoint(new FailedAuthenticationEntryPoint())
//                .accessDeniedHandler(new CustomAccessDeniedHandler())
//            );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return ((request, response, authentication) -> {
            DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

            String id = defaultOAuth2User.getAttributes().get("id").toString();
            String body = """
                {"id":"%s"}
                """.formatted(id);

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());

            PrintWriter writer = response.getWriter();
            writer.println(body);
            writer.flush();
        });
    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient(CustomRequestEntityConverter customRequestEntityConverter) {
        DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();
        accessTokenResponseClient.setRequestEntityConverter(customRequestEntityConverter);

        return accessTokenResponseClient;
    }

    /**
     * cors 설정.
     */
    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(false);
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addExposedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

    class FailedAuthenticationEntryPoint implements AuthenticationEntryPoint {

        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"code\": \"NP\", \"message\": \"No Permission.\"}");
        }
    }

    class CustomAccessDeniedHandler implements AccessDeniedHandler {

        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter()
                .write("Access Denied: You don't have permission to access this resource.");
        }
    }
}
