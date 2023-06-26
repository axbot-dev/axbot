package com.github.axiangcoding.app.server.configuration;


import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecuritySchemes({
        @SecurityScheme(name = "api-key", type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.HEADER),
})
public class SwaggerConfiguration {
    @Bean
    public OpenAPI openApi() {
        String version = System.getenv("APP_VERSION");
        License license = new License();
        license.setName("GNU General Public License v3.0");
        license.setUrl("https://github.com/axiangcoding/AXBot/blob/master/LICENSE");
        Contact contact = new Contact();
        contact.setName("开发者");
        contact.setEmail("axiangcoding@gmail.com");
        return new OpenAPI()
                .info(new Info().title("AXBot V2 API文档")
                        .description("AXBot V2是基于V1重构后的工程，提供了更加规范的编码和更加优雅的设计")
                        .version(version)
                        .license(license)
                        .contact(contact));
    }
}
