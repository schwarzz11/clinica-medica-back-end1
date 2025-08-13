package br.edu.imepac.comum.dtos.auth;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String usuario;
    private String senha;
}