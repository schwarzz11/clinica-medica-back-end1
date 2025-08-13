package br.edu.imepac.administrativo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Indica que esta classe é um componente gerido pelo Spring.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Extrai o cabeçalho "Authorization" da requisição.
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // 2. Verifica se o cabeçalho existe e se começa com "Bearer ".
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Se não, continua para o próximo filtro.
            return;
        }

        // 3. Extrai o token JWT do cabeçalho.
        jwt = authHeader.substring(7);
        username = jwtService.extractUsername(jwt); // Extrai o nome de utilizador do token.

        // 4. Se o utilizador foi extraído e ainda não está autenticado no contexto de segurança...
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Carrega os detalhes do utilizador do banco de dados.
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 5. Se o token for válido...
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // Cria um objeto de autenticação.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // Não precisamos das credenciais (senha) aqui.
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                // Atualiza o SecurityContextHolder com o novo objeto de autenticação.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // 6. Passa a requisição para o próximo filtro na cadeia.
        filterChain.doFilter(request, response);
    }
}