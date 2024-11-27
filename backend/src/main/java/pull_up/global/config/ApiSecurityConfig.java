package pull_up.global.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pull_up.domain.auth.Role;
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
public class ApiSecurityConfig {

    @Value("${auth.domain.frontend}")
    private String frontendDomain;

    @Value("${auth.with-credential}")
    private Boolean withCredential;

    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final AuthenticationExceptionHandler exceptionHandler;
    private final AuthorizationEntryPoint authorizationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/api/**")

                // for cors
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(CsrfConfigurer::disable)
                .formLogin(FormLoginConfigurer::disable)

                // for oauth2
                .oauth2Login(oauth2 -> oauth2
                    .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
                    .successHandler(oAuth2SuccessHandler)
                )

                // authorization
                .addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/oauth2/**").permitAll()
                        .anyRequest().permitAll()
                )

                .exceptionHandling(exception ->
                        exception.accessDeniedHandler(exceptionHandler)
                                .authenticationEntryPoint(authorizationEntryPoint))

                .build();
    }

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> registration(JwtAuthenticationFilter filter) {
        FilterRegistrationBean<JwtAuthenticationFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowCredentials(withCredential);
        corsConfiguration.setAllowPrivateNetwork(withCredential);

        corsConfiguration.addAllowedOrigin(frontendDomain);
        corsConfiguration.addAllowedOrigin("https://pullup-api.shop");
        corsConfiguration.addAllowedOrigin("https://appleid.apple.com");
        corsConfiguration.addAllowedOrigin("https://kauth.kakao.com");
        corsConfiguration.addAllowedOrigin("https://kapi.kakao.com");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.setExposedHeaders((List.of("Authorization", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials")));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}
