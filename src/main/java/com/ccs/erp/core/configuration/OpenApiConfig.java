package com.ccs.erp.core.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Customiza as configurações da Home do Swagger.
 *
 * @author Cleber.Souza
 * @version 1.0
 * @since 12/2022
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        var contato = new Contact();
        contato.email("ccs1201@gmail.com")
                .name("Cleber Souza")
                .url("https://www.linkedin.com/in/ccs1201/");

        return new OpenAPI()
                .components(new Components())
                .info(new Info().title("Desafio Técnico Senior Sistemas - ERP").description(
                                "Esta é uma API REST assíncrona com implementação de Hyper Media(HATEOAS)")
                        .contact(contato));
    }
}