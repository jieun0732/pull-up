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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pull_up.domain.auth.Role;
import pull_up.domain.auth.service.CustomOAuth2UserService;
import pull_up.global.security.filter.JwtAuthenticationFilter;
import pull_up.global.security.handler.AuthenticationExceptionHandler;
import pull_up.global.security.handler.AuthorizationEntryPoint;
import pull_up.global.security.handler.OAuth2SuccessHandler;

import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AdminSecurityConfig {

    private final AuthenticationExceptionHandler exceptionHandler;
    private final AuthorizationEntryPoint authorizationEntryPoint;

    @Value("${auth.admin.id}")
    private String adminId;

    @Value("${auth.admin.password}")
    private String adminPassword;

    @Bean
    @Order(2)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/admin/**")
                .csrf(CsrfConfigurer::disable)
                .formLogin(form -> form.loginPage("/admin/login"))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/admin/login").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                )

                .exceptionHandling(exception ->
                        exception.accessDeniedHandler(exceptionHandler)
                                .authenticationEntryPoint(authorizationEntryPoint))

                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails userDetails = User.withDefaultPasswordEncoder()
                .username(adminId)
                .password(adminPassword)
                .roles(Role.ADMIN.name())
                .build();

        return new InMemoryUserDetailsManager(userDetails);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
