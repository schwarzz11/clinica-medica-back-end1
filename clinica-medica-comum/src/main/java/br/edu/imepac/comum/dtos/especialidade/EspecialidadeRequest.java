package br.edu.imepac.comum.dtos.especialidade;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EspecialidadeRequest {

    @NotBlank(message = "O nome da especialidade é obrigatório.")
    @Size(max = 100, message = "O nome da especialidade deve ter até 100 caracteres.")
    private String nome;
}
