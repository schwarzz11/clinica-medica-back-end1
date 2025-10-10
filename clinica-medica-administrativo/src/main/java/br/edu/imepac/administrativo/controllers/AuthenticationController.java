package br.edu.imepac.administrativo.controllers;

import br.edu.imepac.administrativo.security.AuthenticationService;
import br.edu.imepac.comum.dtos.auth.AuthenticationRequest;
import br.edu.imepac.comum.dtos.auth.AuthenticationResponse;
import br.edu.imepac.comum.dtos.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth") // O caminho base para todos os endpoints de autenticação
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Endpoint para autenticar um utilizador.
     * Recebe um utilizador e senha e, se forem válidos, retorna um token JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        AuthenticationResponse response = authenticationService.authenticate(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Autenticação realizada com sucesso."));
    }
}