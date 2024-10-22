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
import pull_up.global.Oauth.*;
import pull_up.global.Oauth.v2.CustomAccessTokenEmitter;
import pull_up.global.Oauth.v2.OAuth2SuccessHandlerV2;
import pull_up.global.Oauth.v2.OAuth2UserServiceV2;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAccessTokenEmitter customAccessTokenEmitter;
    private final OAuth2UserServiceV2 oAuth2UserServiceV2;
    private final OAuth2SuccessHandlerV2 oAuth2SuccessHandlerV2;
    private final OAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final AppleProperties appleProperties;
    @Bean
    public CustomRequestEntityConverter customRequestEntityConverter() {
        return new CustomRequestEntityConverter(appleProperties);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SecurityFilterChain oauth2SecurityFilterChain) throws Exception {
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
          /*
            .oauth2Login(oauth2 -> oauth2
                .tokenEndpoint(tokenEndpointConfig -> tokenEndpointConfig.accessTokenResponseClient(accessTokenResponseClient(customRequestEntityConverter())))
//                .redirectionEndpoint(endpoint -> endpoint.baseUri("/oauth2/callback/*"))
                .redirectionEndpoint(endpoint -> endpoint.baseUri("/login/oauth2/code/*"))
                .userInfoEndpoint(endpoint -> endpoint.userService(oAuth2UserService))
                .successHandler(oAuth2SuccessHandler)
            )
           */

          /*
           * TODO
           * 1. 요청의 Token 보유여부 확인
           * 2. Token 보유 시 애플에 요청 전송해서 UserInfo 받아옴
           * 3. UserInfo 통해서 DB에 해당 사용자가 가입되어 있는지 확인
           * 4. 가입되어 있으면 로그인, 가입되어 있지 않으면 회원가입
           * 5. Handler 통해서 회원정보 전송
           */
          .oauth2Login(oauth2 -> oauth2
            .tokenEndpoint(tokenConfig -> tokenConfig.accessTokenResponseClient(customAccessTokenEmitter.emit()))
              .redirectionEndpoint(customEndPoint -> customEndPoint.baseUri("/login/oauth2/code/*"))
              .userInfoEndpoint(customEndPoint -> customEndPoint.userService(oAuth2UserServiceV2))
              .successHandler(oAuth2SuccessHandlerV2)
            )

            .authorizeHttpRequests(request -> request
                .requestMatchers("/api/pull-up/oauth2/**", "/api/pull-up/login")
                .permitAll()
                .requestMatchers("/api/pull-up/swagger-ui/**").permitAll()
                .requestMatchers("/api/pull-up/swagger-ui/index.html").permitAll()
                .requestMatchers("/api/pull-up/lawsuit/**").permitAll()
                .requestMatchers("/api/pull-up/").permitAll()
                .requestMatchers("/").permitAll()
                .anyRequest().permitAll()
            )

            // 인증 예외 처리
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(new FailedAuthenticationEntryPoint())
                .accessDeniedHandler(new CustomAccessDeniedHandler())
            );

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

        // Allow credentials if needed
        corsConfiguration.setAllowCredentials(true);

        // Add allowed origins (replace with actual origins for your application)
        corsConfiguration.addAllowedOriginPattern("http://localhost:8080"); // Swagger UI URL
        corsConfiguration.addAllowedOriginPattern("https://pullup-api.shop"); // Production server URL

        // Allow all headers, methods, and expose specific headers if necessary
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addExposedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

    static class FailedAuthenticationEntryPoint implements AuthenticationEntryPoint {

        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"code\": \"NP\", \"message\": \"No Permission.\"}");
        }
    }

    static class CustomAccessDeniedHandler implements AccessDeniedHandler {

        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter()
                .write("Access Denied: You don't have permission to access this resource.");
        }
    }
}
