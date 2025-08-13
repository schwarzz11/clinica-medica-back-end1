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

    private final FuncionarioRepository funcionarioRepository;

    /**
     * Define como o Spring Security deve carregar os detalhes de um usuário.
     * Quando o Spring precisa encontrar um usuário pelo seu nome de utilizador, ele usará este método.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> funcionarioRepository.findByUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilizador não encontrado"));
    }

    /**
     * Define o "provedor de autenticação".
     * Ele junta o UserDetailsService (para encontrar o utilizador) e o PasswordEncoder (para verificar a senha).
     */
    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    /**
     * Expõe o AuthenticationManager do Spring como um Bean.
     * Este é o componente central que processa uma requisição de autenticação.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}