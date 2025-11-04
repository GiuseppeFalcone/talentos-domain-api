package com.certimetergroup.easycv.domainapi.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Domain API")
                        .version("v1.0")
                        .description("API for domain RESTful api")
                        .contact(new Contact().name("Giuseppe Falcone").email("giuseppe.falcone@lutech.it"))
                        .license(new License().name("MIT").url("https://opensource.org/licenses/MIT"))
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Developer guide")
                        .url("http://localhost:8080/user-api/docs"));
    }
}
