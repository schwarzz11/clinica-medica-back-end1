package br.edu.imepac.administrativo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // IMPORTAR ESTA CLASSE
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Permite que qualquer um aceda ao nosso endpoint de login.
                        .requestMatchers("/auth/**").permitAll()

                        // =======================================================
                        // Permite a criação do primeiro perfil e funcionário sem autenticação. // APAGAR QUANDO TIVER UM BANCO DE DADOS FIXO. ATUALMENTE EM FASE DE TESTES.
                        // =======================================================
                        .requestMatchers(HttpMethod.POST, "/perfis", "/funcionarios").permitAll()

                        // Qualquer outra requisição para qualquer outro endpoint deve ser autenticada.
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}