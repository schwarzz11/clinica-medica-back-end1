package br.edu.imepac.comum.dtos.paciente;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Data
public class PacienteRequest {

    @NotBlank(message = "O nome do paciente é obrigatório.")
    private String nome;

    @NotBlank(message = "O CPF é obrigatório.")
    @CPF(message = "Informe um CPF válido.")
    private String cpf;

    @NotNull(message = "O sexo é obrigatório.")
    private Character sexo;

    @NotBlank(message = "O RG é obrigatório.")
    private String rg;

    @NotBlank(message = "O órgão emissor é obrigatório.")
    private String orgaoEmissor;

    @NotNull(message = "A data de nascimento é obrigatória.")
    @Past(message = "A data de nascimento deve estar no passado.")
    private LocalDate dataNascimento;

    @NotBlank(message = "A rua é obrigatória.")
    private String rua;

    @NotBlank(message = "O número é obrigatório.")
    private String numero;

    private String complemento;

    @NotBlank(message = "O bairro é obrigatório.")
    private String bairro;

    @NotBlank(message = "A cidade é obrigatória.")
    private String cidade;

    @NotBlank(message = "O estado é obrigatório.")
    @Size(min = 2, max = 2, message = "Informe a sigla do estado com dois caracteres.")
    private String estado;

    @NotBlank(message = "O telefone é obrigatório.")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "O telefone deve conter 10 ou 11 dígitos.")
    private String telefone;

    @Pattern(regexp = "^$|[0-9]{10,11}$", message = "O celular deve conter 10 ou 11 dígitos.")
    private String celular;

    @Email(message = "Informe um e-mail válido.")
    private String email;

    private boolean possuiConvenio;

    @Positive(message = "O convênio informado é inválido.")
    private Long convenioId; // Apenas o ID do convênio

    private String numeroCarteirinha;

    @Future(message = "A validade da carteirinha deve estar no futuro.")
    private LocalDate validadeCarteirinha;

    @AssertTrue(message = "O convênio, o número e a validade da carteirinha são obrigatórios quando o paciente possui convênio.")
    public boolean isConvenioValido() {
        if (!possuiConvenio) {
            return true;
        }

        return convenioId != null
                && numeroCarteirinha != null && !numeroCarteirinha.isBlank()
                && validadeCarteirinha != null;
    }
}
