package br.edu.imepac.comum.dtos.paciente;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PacienteDto {
    private Long id;
    private String nome;
    private String cpf;
    private char sexo;
    private String rg;
    private String orgaoEmissor;
    private LocalDate dataNascimento;
    private String telefone;
    private String celular;
    private String email;
    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private boolean possuiConvenio;
    private Long convenioId;
    private String nomeConvenio;
    private String numeroCarteirinha;
    private LocalDate validadeCarteirinha;
}
