package pull_up;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@OpenAPIDefinition(
	servers = {
		@Server(url="/api/pull-up/", description = "Default Server url")
	}
)
@SpringBootApplication
public class PullUpApplication {

	public static void main(String[] args) {
		SpringApplication.run(PullUpApplication.class, args);
	}

}
