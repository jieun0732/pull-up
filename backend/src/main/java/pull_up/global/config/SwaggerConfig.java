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
                .addServersItem(new Server().url("https://pullup-api.shop").description("https 요청 server"))
                .addServersItem(new Server().url("http://43.203.236.62:8080").description("요청 server"))
                .addServersItem(new Server().url("http://localhost:8080").description("Local server"))
                .info(new Info()
                        .title("pull-up API")
                        .version("1.2")
                        .description("모의고사를 통한 인적성 문제 능력 향상"));
    }
}

