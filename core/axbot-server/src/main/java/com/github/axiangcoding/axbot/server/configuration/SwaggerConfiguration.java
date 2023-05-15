package com.github.axiangcoding.axbot.server.configuration;


import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecuritySchemes({
        @SecurityScheme(name = "api-key",
                type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.HEADER),
})
public class SwaggerConfiguration {
    @Value("${server.servlet.context-path:}")
    String contextPath;

    @Bean
    public OpenAPI openApi() {
        String appVersion = System.getenv("APP_VERSION");
        return new OpenAPI().
                addServersItem(new Server().url(contextPath))
                .info(new Info().title("AXBot API")
                        .description("AXBot api description")
                        .version(appVersion));
    }
}
