package pull_up.global.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.oauth2.client.oidc.authentication.OidcIdTokenDecoderFactory;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoderFactory;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pull_up.global.oauth.v2.handler.OAuth2SuccessHandlerV2;
import pull_up.global.oauth.v2.service.OAuth2UserServiceV2;
import pull_up.global.oauth.v2.util.CustomAccessTokenConverter;

@Slf4j
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2UserServiceV2 oAuth2UserService;
    private final OAuth2SuccessHandlerV2 oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(CsrfConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)

                .oauth2Login(oauth2 -> oauth2
                    .redirectionEndpoint(redirect -> redirect.baseUri("/api/pull-up/oauth2/callback/kakao"))
                    .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
                    .successHandler(oAuth2SuccessHandler)
                )

                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/pull-up/oauth2/**")
                        .permitAll()

                        .requestMatchers("/api/pull-up/lawsuit/**").permitAll()

                        .anyRequest()
                        .permitAll()
                )
                .build();
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowCredentials(false);
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addExposedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}
