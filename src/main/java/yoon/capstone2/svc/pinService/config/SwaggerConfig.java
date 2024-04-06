package yoon.capstone2.svc.pinService.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi pinApiGroup(){
        return GroupedOpenApi.builder()
                .group("PIN API")
                .pathsToMatch("/api/v1/pins/**")
                .build();
    }

}
