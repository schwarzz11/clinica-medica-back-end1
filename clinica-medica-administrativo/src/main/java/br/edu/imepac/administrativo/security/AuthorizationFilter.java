package br.edu.imepac.administrativo.security;

import br.edu.imepac.administrativo.exceptions.AuthenticationClinicaMedicaException;
import br.edu.imepac.comum.services.PerfilService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

// Para ativar este filtro, você precisará registrá-lo em uma classe de configuração de segurança.
// Por enquanto, a anotação @Component está comentada.
//@Component
public class AuthorizationFilter extends OncePerRequestFilter {

    private final PerfilService perfilService;

    public AuthorizationFilter(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();

        // Libera o acesso à documentação do Swagger e outras rotas públicas se necessário
        if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return; // Importante: retorna para não executar o resto do filtro
        }

        // Recupera os cabeçalhos para autenticação e autorização
        String usuario = Optional.ofNullable(request.getHeader("usuario"))
                .orElseThrow(() -> new AuthenticationClinicaMedicaException("Cabeçalho 'usuario' não encontrado!"));

        String senha = Optional.ofNullable(request.getHeader("senha"))
                .orElseThrow(() -> new AuthenticationClinicaMedicaException("Cabeçalho 'senha' não encontrado!"));

        String acao = Optional.ofNullable(request.getHeader("action"))
                .orElseThrow(() -> new AuthenticationClinicaMedicaException("Cabeçalho 'action' não encontrado!"));

        // *** CORREÇÃO APLICADA AQUI ***
        // Chamando o método na instância 'perfilService' em vez da classe 'PerfilService'.
        boolean autorizado = this.perfilService.verificarAutorizacao(usuario, senha, acao);

        // Se o serviço retornar 'false', o usuário não tem permissão.
        if (!autorizado) {
            // Lança uma exceção que será capturada pelo handler global para retornar um erro 403 Forbidden ou similar.
            throw new AuthenticationClinicaMedicaException("O usuário não tem permissão para realizar a ação: " + acao);
        }

        // Se a autorização foi bem-sucedida, a requisição continua seu fluxo normal.
        filterChain.doFilter(request, response);
    }
}
