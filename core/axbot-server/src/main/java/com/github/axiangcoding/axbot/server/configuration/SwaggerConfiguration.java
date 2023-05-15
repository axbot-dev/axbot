package com.github.axiangcoding.axbot.server.configuration;


import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecuritySchemes({
        @SecurityScheme(name = "api-key",
                type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.HEADER),
})
public class SwaggerConfiguration {
    @Bean
    public OpenAPI openApi() {
        String appVersion = System.getenv("APP_VERSION");
        return new OpenAPI()
                .info(new Info().title("AXBot API文档")
                        .description("这里列出的是AXBot的接口，包括了机器人的接口和管理后台的接口，如果接口需要权限，请咨询开发者应该如何调用。")
                        .version(appVersion));
    }
}
