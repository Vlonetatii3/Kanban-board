package mmach.tasks.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI()
                .info(new Info().title("Task Manager app")
                        .description("Task manager project")
                        .version("1.0.0")
                        .contact( new Contact()
                                .name("Santiago Machuca")
                                .email("santi.machuca@gmail.com")
                        ));
    }
}
