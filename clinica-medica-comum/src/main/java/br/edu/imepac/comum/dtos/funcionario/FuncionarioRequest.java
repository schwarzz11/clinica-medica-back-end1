package br.edu.imepac.comum.dtos.funcionario;

import br.edu.imepac.comum.domain.EnumTipoFuncionario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Data
public class FuncionarioRequest {

    @NotBlank(message = "O nome do funcionário não pode estar em branco.")
    private String nome;

    @NotBlank(message = "O CPF não pode estar em branco.")
    @CPF(message = "Informe um CPF válido.")
    private String cpf;

    @NotBlank(message = "O e-mail não pode estar em branco.")
    @Email(message = "Informe um e-mail válido.")
    private String email;

    @NotBlank(message = "O usuário não pode estar em branco.")
    @Size(min = 4, max = 50, message = "O usuário deve ter entre 4 e 50 caracteres.")
    private String usuario;

    @NotBlank(message = "A senha não pode estar em branco.")
    private String senha;

    @NotNull(message = "O sexo é obrigatório.")
    private Character sexo;

    @NotNull(message = "A data de nascimento é obrigatória.")
    @Past(message = "A data de nascimento deve estar no passado.")
    private LocalDate dataNascimento;

    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;

    @Size(min = 2, max = 2, message = "Informe a sigla do estado com dois caracteres.")
    private String estado;

    @NotNull(message = "O tipo de funcionário é obrigatório.")
    private EnumTipoFuncionario tipoFuncionario;

    @NotNull(message = "O ID do perfil é obrigatório.")
    private Long perfilId;
}
