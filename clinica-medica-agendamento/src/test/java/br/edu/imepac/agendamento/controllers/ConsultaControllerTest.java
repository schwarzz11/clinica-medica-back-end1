package br.edu.imepac.agendamento.controllers;

import br.edu.imepac.comum.dtos.consulta.ConsultaDto;
import br.edu.imepac.comum.dtos.consulta.ConsultaRequest;
import br.edu.imepac.comum.exceptions.GlobalExceptionHandler;
import br.edu.imepac.comum.services.ConsultaService;
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

@WebMvcTest(ConsultaController.class)
@Import(GlobalExceptionHandler.class)
class ConsultaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ConsultaService consultaService;

    @Test
    void deveAgendarConsultaComSucesso() throws Exception {
        ConsultaRequest request = criarConsultaRequestValida();

        ConsultaDto consultaDto = new ConsultaDto();
        consultaDto.setId(10L);
        consultaDto.setNomePaciente("João da Silva");
        consultaDto.setNomeMedico("Dra. Ana");
        consultaDto.setDataHorario(request.getDataHorario());
        consultaDto.setERetorno(request.isERetorno());
        consultaDto.setEstaAtiva(true);

        when(consultaService.save(ArgumentMatchers.any(ConsultaRequest.class))).thenReturn(consultaDto);

        mockMvc.perform(post("/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Consulta agendada com sucesso."))
                .andExpect(jsonPath("$.data.nomePaciente").value("João da Silva"));
    }

    @Test
    void deveRetornarErroQuandoPacienteNaoInformado() throws Exception {
        ConsultaRequest request = criarConsultaRequestValida();
        request.setPacienteId(null);

        mockMvc.perform(post("/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Erro de validação"))
                .andExpect(jsonPath("$.data[?(@.field=='pacienteId')].message", hasItem("O paciente é obrigatório.")));

        verifyNoInteractions(consultaService);
    }

    private ConsultaRequest criarConsultaRequestValida() {
        ConsultaRequest request = new ConsultaRequest();
        request.setPacienteId(1L);
        request.setMedicoId(2L);
        request.setDataHorario(LocalDateTime.now().plusDays(1));
        request.setSintomas("Dor de cabeça");
        request.setERetorno(false);
        return request;
    }
}
