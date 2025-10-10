package br.edu.imepac.agendamento.controllers;

import br.edu.imepac.comum.dtos.consulta.ConsultaDto;
import br.edu.imepac.comum.dtos.consulta.ConsultaRequest;
import br.edu.imepac.comum.dtos.responses.ApiResponse;
import br.edu.imepac.comum.services.ConsultaService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Exposição das operações de agendamento de consultas com respostas padronizadas e
 * instrumentação para métricas e logs estruturados.
 */
@Slf4j
@RestController
@RequestMapping("/consultas")
@RequiredArgsConstructor
@Tag(name = "Consultas", description = "Gestão de consultas médicas")
public class ConsultaController {

    private final ConsultaService consultaService;

    @PostMapping
    public ResponseEntity<ApiResponse<ConsultaDto>> save(@Valid @RequestBody ConsultaRequest consultaRequest) {
        log.info("Agendando nova consulta para paciente {}", consultaRequest.getPacienteId());
        ConsultaDto novaConsulta = consultaService.save(consultaRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(novaConsulta, "Consulta agendada com sucesso."));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ConsultaDto>>> listAll() {
        return ResponseEntity.ok(ApiResponse.success(
                consultaService.findAll(),
                "Consultas recuperadas com sucesso."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ConsultaDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                consultaService.findById(id),
                "Consulta recuperada com sucesso."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ConsultaDto>> update(@PathVariable Long id,
                                                            @Valid @RequestBody ConsultaRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                consultaService.update(id, request),
                "Consulta atualizada com sucesso."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> cancel(@PathVariable Long id) {
        log.info("Cancelando consulta {}", id);
        consultaService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Consulta cancelada com sucesso."));
    }
}
