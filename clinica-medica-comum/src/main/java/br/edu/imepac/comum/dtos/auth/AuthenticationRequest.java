package br.edu.imepac.comum.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthenticationRequest {

    @NotBlank(message = "O usuário é obrigatório.")
    private String usuario;

    @NotBlank(message = "A senha é obrigatória.")
    private String senha;
}