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
//                .addServersItem(new Server().url("https://k10a406.p.ssafy.io/api/").description("요청 서버"))
                .addServersItem(new Server().url("http://localhost:8000/api/").description("Local server"))
                .info(new Info()
                        .title("pull-up")
                        .version("1.0")
                        .description("모의고사를 통한 인적성 문제 능력 향상"));
    }
}

