package br.edu.imepac.comum.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
    private AuthenticatedUserDto user;
    private Map<String, Boolean> permissions;
}
