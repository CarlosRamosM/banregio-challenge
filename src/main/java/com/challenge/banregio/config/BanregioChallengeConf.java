package com.challenge.banregio.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BanregioChallengeConf {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("banregio-challenge")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Banregio Challenge API")
                .description("API de prueba para el desafío de Banregio")
                .version("v1.0.0")
                .license(new io.swagger.v3.oas.models.info.License()
                    .name("Apache 2.0")
                    .url("http://springdoc.org")
                )
                .termsOfService("http://swagger.io/terms/")
                .contact(new io.swagger.v3.oas.models.info.Contact()
                    .email("ramos.montoya.carlos@gmail.com")
                    .name("Carlos Ramos Montoya")
                    .url("https://github.com/carlosramosm")
                )
            );
    }
}
