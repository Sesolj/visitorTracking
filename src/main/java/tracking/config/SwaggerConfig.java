package tracking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String API_NAME = "방문자 수 트래킹 서비스";
    private static final String API_DESCRIPTION = "방문자 수 트래킹 서비스 API Docs";

    @Bean
    public OpenAPI openAPI() {

        Info info = new Info()
                .title(API_NAME)
                .description(API_DESCRIPTION);

        return new OpenAPI()
                .info(info);
    }
}
