package br.edu.imepac.atendimento.controllers;

import br.edu.imepac.comum.dtos.prontuario.ProntuarioDto;
import br.edu.imepac.comum.dtos.prontuario.ProntuarioRequest;
import br.edu.imepac.comum.exceptions.GlobalExceptionHandler;
import br.edu.imepac.comum.services.ProntuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProntuarioController.class)
@Import(GlobalExceptionHandler.class)
class ProntuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProntuarioService prontuarioService;

    @Test
    void deveCriarProntuarioComSucesso() throws Exception {
        ProntuarioRequest request = criarProntuarioRequestValido();

        ProntuarioDto prontuarioDto = new ProntuarioDto();
        prontuarioDto.setId(5L);
        prontuarioDto.setConsultaId(request.getConsultaId());
        prontuarioDto.setDataConsulta(LocalDateTime.now());
        prontuarioDto.setNomePaciente("João da Silva");
        prontuarioDto.setNomeMedico("Dra. Ana");
        prontuarioDto.setReceituario("Paracetamol");

        when(prontuarioService.save(ArgumentMatchers.any(ProntuarioRequest.class))).thenReturn(prontuarioDto);

        mockMvc.perform(post("/prontuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Prontuário criado com sucesso."))
                .andExpect(jsonPath("$.data.consultaId").value(request.getConsultaId()));
    }

    @Test
    void deveRetornarErroQuandoConsultaNaoInformada() throws Exception {
        ProntuarioRequest request = criarProntuarioRequestValido();
        request.setConsultaId(null);

        mockMvc.perform(post("/prontuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Erro de validação"))
                .andExpect(jsonPath("$.data[?(@.field=='consultaId')].message", hasItem("O ID da consulta não pode ser nulo.")));

        verifyNoInteractions(prontuarioService);
    }

    private ProntuarioRequest criarProntuarioRequestValido() {
        ProntuarioRequest request = new ProntuarioRequest();
        request.setConsultaId(3L);
        request.setReceituario("Repouso e hidratação");
        request.setExames("Hemograma");
        request.setObservacoes("Paciente em acompanhamento");
        return request;
    }
}
