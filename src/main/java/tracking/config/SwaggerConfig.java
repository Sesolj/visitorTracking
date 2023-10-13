package tracking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String API_NAME = "Swagger 테스트";
    private static final String API_DESCRIPTION = "Swagger 테스트입니다";

    @Bean
    public OpenAPI openAPI() {

        Info info = new Info()
                .title(API_NAME)
                .description(API_DESCRIPTION);

        return new OpenAPI()
                .info(info);
    }
}
