package br.edu.imepac.comum.dtos.auth;

import br.edu.imepac.comum.domain.EnumTipoFuncionario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticatedUserDto {
    private Long id;
    private String nome;
    private String usuario;
    private String email;
    private EnumTipoFuncionario tipoFuncionario;
    private Long perfilId;
    private String perfilNome;
}
