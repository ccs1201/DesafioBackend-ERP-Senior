package com.ccs.erp.core.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Classe de configuração de cors (Cross Oring Resource Sharing)
 *
 * @author Cleber Souza
 * @version 1.0
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS">Cross-Origin Resource Sharing (CORS) on Mozila Developer</a>
 * @since 20/08/2022
 */

@Configuration
public class CorsMappingConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**");
//                .allowedMethods("*");
    }
}
