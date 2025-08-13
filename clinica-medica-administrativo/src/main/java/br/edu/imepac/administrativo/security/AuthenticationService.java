package br.edu.imepac.administrativo.security;

import br.edu.imepac.comum.dtos.auth.AuthenticationRequest;
import br.edu.imepac.comum.dtos.auth.AuthenticationResponse;
import br.edu.imepac.comum.repositories.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final FuncionarioRepository repository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Autentica um utilizador e retorna um token JWT se as credenciais estiverem corretas.
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // O AuthenticationManager verifica se o utilizador existe e se a senha está correta.
        // Se algo estiver errado, ele lança uma exceção que será tratada pelo nosso handler global.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsuario(),
                        request.getSenha()
                )
        );

        // Se a autenticação for bem-sucedida, busca o utilizador para gerar o token.
        var user = repository.findByUsuario(request.getUsuario())
                .orElseThrow(); // Não precisa de orElseThrow com mensagem, pois o manager já validou.

        // Gera o token JWT.
        var jwtToken = jwtService.generateToken(user);

        // Retorna o token na resposta.
        return new AuthenticationResponse(jwtToken);
    }
}