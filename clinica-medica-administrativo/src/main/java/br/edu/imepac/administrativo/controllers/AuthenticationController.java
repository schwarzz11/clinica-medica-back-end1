package br.edu.imepac.administrativo.controllers;

import br.edu.imepac.administrativo.security.AuthenticationService;
import br.edu.imepac.comum.dtos.auth.AuthenticationRequest;
import br.edu.imepac.comum.dtos.auth.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        // --- NOSSO ESPIÃO Nº 1 ---
        // Vamos imprimir o que recebemos do front-end.
        System.out.println("======================================================");
        System.out.println("Tentativa de login recebida para o utilizador: " + request.getUsuario());
        System.out.println("======================================================");

        try {
            AuthenticationResponse response = service.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            // --- NOSSO ESPIÃO Nº 2 ---
            // Se a autenticação falhar, vamos imprimir o erro exato.
            System.err.println("Falha na autenticação: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Utilizador ou senha inválidos."));
        }
    }
}