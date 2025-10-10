package br.edu.imepac.atendimento.controllers;

import br.edu.imepac.comum.dtos.prontuario.ProntuarioDto;
import br.edu.imepac.comum.dtos.prontuario.ProntuarioRequest;
import br.edu.imepac.comum.dtos.responses.ApiResponse;
import br.edu.imepac.comum.services.ProntuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
 * Endpoints de prontuários clínicos com validações e integração de observabilidade
 * compartilhada entre os microsserviços.
 */
@Slf4j
@RestController
@RequestMapping("/prontuarios")
@RequiredArgsConstructor
@Tag(name = "Prontuários", description = "Gestão de prontuários clínicos")
public class ProntuarioController {

    private final ProntuarioService prontuarioService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProntuarioDto>>> getAllProntuarios() {
        return ResponseEntity.ok(ApiResponse.success(
                prontuarioService.findAll(),
                "Prontuários recuperados com sucesso."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProntuarioDto>> getProntuarioById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                prontuarioService.findById(id),
                "Prontuário recuperado com sucesso."));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProntuarioDto>> createProntuario(@Valid @RequestBody ProntuarioRequest request) {
        log.info("Criando prontuário para consulta {}", request.getConsultaId());
        ProntuarioDto createdProntuario = prontuarioService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdProntuario, "Prontuário criado com sucesso."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProntuarioDto>> updateProntuario(@PathVariable Long id,
                                                                       @Valid @RequestBody ProntuarioRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                prontuarioService.update(id, request),
                "Prontuário atualizado com sucesso."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProntuario(@PathVariable Long id) {
        log.info("Removendo prontuário {}", id);
        prontuarioService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Prontuário removido com sucesso."));
    }
}
