package br.edu.imepac.administrativo.security;

import br.edu.imepac.comum.repositories.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final FuncionarioRepository repository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            // --- NOSSO ESPIÃO Nº 3 ---
            // Vamos ver qual utilizador o Spring está a tentar encontrar.
            System.out.println("======================================================");
            System.out.println("A procurar na base de dados pelo utilizador: " + username);
            System.out.println("======================================================");

            var user = repository.findByUsuario(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Utilizador não encontrado na base de dados!"));

            // --- NOSSO ESPIÃO Nº 4 ---
            System.out.println("Utilizador '" + username + "' encontrado com sucesso!");
            return user;
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}