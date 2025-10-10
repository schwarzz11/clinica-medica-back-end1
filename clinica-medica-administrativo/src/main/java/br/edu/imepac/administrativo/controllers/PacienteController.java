package br.edu.imepac.administrativo.controllers;

import br.edu.imepac.comum.dtos.paciente.PacienteDto;
import br.edu.imepac.comum.dtos.paciente.PacienteRequest;
import br.edu.imepac.comum.dtos.responses.ApiResponse;
import br.edu.imepac.comum.services.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.tags.Tag;
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
 * Controlador responsável pelo fluxo de CRUD de pacientes com validações e respostas
 * padronizadas. Toda solicitação é observada para geração de métricas e logs
 * estruturados.
 */
@Slf4j
@RestController
@RequestMapping("/pacientes")
@RequiredArgsConstructor
@Tag(name = "Pacientes", description = "Gestão de pacientes e seus dados cadastrais")
public class PacienteController {

    private final PacienteService pacienteService;

    @PostMapping
    public ResponseEntity<ApiResponse<PacienteDto>> save(@Valid @RequestBody PacienteRequest pacienteRequest) {
        log.info("Requisição para criar paciente: {}", pacienteRequest.getNome());
        PacienteDto novoPaciente = pacienteService.save(pacienteRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(novoPaciente, "Paciente criado com sucesso."));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PacienteDto>>> listAll() {
        return ResponseEntity.ok(ApiResponse.success(
                pacienteService.findAll(),
                "Pacientes recuperados com sucesso."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PacienteDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                pacienteService.findById(id),
                "Paciente recuperado com sucesso."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PacienteDto>> update(@PathVariable Long id,
                                                           @Valid @RequestBody PacienteRequest pacienteRequest) {
        return ResponseEntity.ok(ApiResponse.success(
                pacienteService.update(id, pacienteRequest),
                "Paciente atualizado com sucesso."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        pacienteService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Paciente removido com sucesso."));
    }
}
