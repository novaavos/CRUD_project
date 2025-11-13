package natixis.crud_project.transfer.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Bank Transfer Scheduling API")
                                .description("API for managing scheduled bank transactions with fee calculation rules.")
                                .version("1.0.0")
                );
    }
}
