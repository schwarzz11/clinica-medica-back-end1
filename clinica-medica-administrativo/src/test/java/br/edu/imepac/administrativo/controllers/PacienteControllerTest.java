package br.edu.imepac.administrativo.controllers;

import br.edu.imepac.administrativo.exceptions.ClinicaMedicaHandleExceptions;
import br.edu.imepac.comum.dtos.paciente.PacienteDto;
import br.edu.imepac.comum.dtos.paciente.PacienteRequest;
import br.edu.imepac.comum.exceptions.GlobalExceptionHandler;
import br.edu.imepac.comum.services.PacienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PacienteController.class)
@Import({GlobalExceptionHandler.class, ClinicaMedicaHandleExceptions.class})
class PacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PacienteService pacienteService;

    @Test
    void deveCriarPacienteComSucesso() throws Exception {
        PacienteRequest request = criarPacienteRequestValido();

        PacienteDto pacienteDto = new PacienteDto();
        pacienteDto.setId(1L);
        pacienteDto.setNome("João da Silva");
        pacienteDto.setCpf(request.getCpf());
        pacienteDto.setSexo('M');
        pacienteDto.setDataNascimento(request.getDataNascimento());
        pacienteDto.setTelefone(request.getTelefone());
        pacienteDto.setCelular(request.getCelular());
        pacienteDto.setEmail(request.getEmail());

        when(pacienteService.save(ArgumentMatchers.any(PacienteRequest.class))).thenReturn(pacienteDto);

        mockMvc.perform(post("/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Paciente criado com sucesso."))
                .andExpect(jsonPath("$.data.nome").value("João da Silva"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deveRetornarErroDeValidacaoQuandoDadosInvalidos() throws Exception {
        PacienteRequest request = criarPacienteRequestValido();
        request.setNome("");

        mockMvc.perform(post("/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Erro de validação"))
                .andExpect(jsonPath("$.data[?(@.field=='nome')].message", hasItem("O nome do paciente é obrigatório.")));

        verifyNoInteractions(pacienteService);
    }

    private PacienteRequest criarPacienteRequestValido() {
        PacienteRequest request = new PacienteRequest();
        request.setNome("João da Silva");
        request.setCpf("52998224725");
        request.setSexo('M');
        request.setRg("1234567");
        request.setOrgaoEmissor("SSP");
        request.setDataNascimento(LocalDate.of(1990, 1, 1));
        request.setRua("Rua Central");
        request.setNumero("123");
        request.setComplemento("Casa");
        request.setBairro("Centro");
        request.setCidade("Uberlândia");
        request.setEstado("MG");
        request.setTelefone("34999888776");
        request.setCelular("34999888776");
        request.setEmail("joao.silva@example.com");
        request.setPossuiConvenio(false);
        return request;
    }
}
