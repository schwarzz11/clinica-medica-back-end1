package br.edu.imepac.comum.dtos.convenio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.br.CNPJ;

@Data
public class ConvenioRequest {

    @NotBlank(message = "O nome da empresa é obrigatório.")
    private String nomeEmpresa;

    @NotBlank(message = "O CNPJ é obrigatório.")
    @CNPJ(message = "Informe um CNPJ válido.")
    private String cnpj;

    @NotBlank(message = "O nome do contato é obrigatório.")
    private String nomeContato;

    @NotBlank(message = "O telefone é obrigatório.")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "O telefone deve conter 10 ou 11 dígitos.")
    private String telefone;
}
