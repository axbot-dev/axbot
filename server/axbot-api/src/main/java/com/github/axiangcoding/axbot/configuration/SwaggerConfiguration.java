package com.github.axiangcoding.axbot.configuration;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public OpenAPI openApi() {
        String appVersion = System.getenv("APP_VERSION");
        return new OpenAPI().info(
                new Info().title("AXBot API")
                        .description("AXBot api description")
                        .version(appVersion));
    }
}
