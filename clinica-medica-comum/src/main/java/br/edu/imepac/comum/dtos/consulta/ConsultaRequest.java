package br.edu.imepac.comum.dtos.consulta;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConsultaRequest {

    @NotNull(message = "O paciente é obrigatório.")
    @Positive(message = "Informe um paciente válido.")
    private Long pacienteId;

    @NotNull(message = "O médico é obrigatório.")
    @Positive(message = "Informe um médico válido.")
    private Long medicoId;

    @NotNull(message = "A data e horário são obrigatórios.")
    @FutureOrPresent(message = "A consulta deve ser agendada para o presente ou futuro.")
    private LocalDateTime dataHorario;

    @NotBlank(message = "Os sintomas são obrigatórios.")
    private String sintomas;

    private boolean eRetorno;
}