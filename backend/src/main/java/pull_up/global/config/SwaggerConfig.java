package pull_up.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url("https://pullup-api.shop").description("Production server"))
                .addServersItem(new Server().url("http://pullup-api.shop:3000").description("Development server"))
                .addServersItem(new Server().url("http://localhost:3000").description("Development server2"))
                .addServersItem(new Server().url("http://localhost:8080").description("Local server"))
                .info(new Info()
                        .title("pull-up API")
                        .version("1.2")
                        .description("모의고사를 통한 인적성 문제 능력 향상"));
    }
}

