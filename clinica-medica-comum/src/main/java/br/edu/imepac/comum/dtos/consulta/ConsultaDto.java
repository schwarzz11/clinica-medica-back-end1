package br.edu.imepac.comum.dtos.consulta;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConsultaDto {
    private Long id;
    private Long pacienteId;
    private Long medicoId;
    private String nomePaciente;
    private String nomeMedico;
    private LocalDateTime dataHorario;
    private boolean eRetorno;
    private boolean estaAtiva;
    private String sintomas;
}
