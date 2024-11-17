package pull_up.global.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pull_up.global.security.handler.AuthenticationExceptionHandler;
import pull_up.global.security.handler.AuthorizationEntryPoint;
import pull_up.global.security.filter.JwtAuthenticationFilter;
import pull_up.global.security.handler.OAuth2SuccessHandler;
import pull_up.domain.auth.service.CustomOAuth2UserService;

import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${auth.domain.frontend}")
    private String frontendDomain;

    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final AuthenticationExceptionHandler exceptionHandler;
    private final AuthorizationEntryPoint authorizationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // for cors
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(CsrfConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)

                // for exception handling
                .exceptionHandling(exception ->
                        exception.accessDeniedHandler(exceptionHandler)
                                .authenticationEntryPoint(authorizationEntryPoint))

                // for oauth2
                .oauth2Login(oauth2 -> oauth2
                    .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
                    .successHandler(oAuth2SuccessHandler)
                )

                // for authorization and authentication
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/admin/**").permitAll()
                        .requestMatchers("/api/oauth2/**").permitAll()
                        .anyRequest().permitAll()
                )

                .build();
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

//        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOrigin(frontendDomain);
        corsConfiguration.addAllowedOrigin("https://pullup-api.shop");
        corsConfiguration.addAllowedOrigin("https://appleid.apple.com");
        corsConfiguration.addAllowedOrigin("https://kauth.kakao.com");
        corsConfiguration.addAllowedOrigin("https://kapi.kakao.com");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.setExposedHeaders((List.of("Authorization", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials")));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // for frontend local test
        corsConfiguration.addAllowedOrigin("http://localhost:3000");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}
