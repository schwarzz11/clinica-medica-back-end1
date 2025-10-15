// api-gateway/src/main/java/br/edu/imepac/apigateway/config/CorsConfig.java

package br.edu.imepac.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Arrays.asList("http://127.0.0.1:5500", "http://localhost:5500"));
        corsConfig.setMaxAge(3600L); // Cache da configuração por 1 hora
        corsConfig.addAllowedMethod("*"); // Permite todos os métodos (GET, POST, etc.)
        corsConfig.addAllowedHeader("*"); // Permite todos os cabeçalhos
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig); // Aplica a configuração a todas as rotas (/**)

        return new CorsWebFilter(source);
    }
}