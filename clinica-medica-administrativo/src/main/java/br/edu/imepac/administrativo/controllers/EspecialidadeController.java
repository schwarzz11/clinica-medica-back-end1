package br.edu.imepac.administrativo.controllers;

import br.edu.imepac.comum.dtos.especialidade.EspecialidadeDto;
import br.edu.imepac.comum.dtos.especialidade.EspecialidadeRequest;
import br.edu.imepac.comum.dtos.responses.ApiResponse;
import br.edu.imepac.comum.services.EspecialidadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequestMapping("/especialidades")
@RequiredArgsConstructor
public class EspecialidadeController {

    private final EspecialidadeService especialidadeService;

    @PostMapping
    public ResponseEntity<ApiResponse<EspecialidadeDto>> salvarEspecialidade(@Valid @RequestBody EspecialidadeRequest request) {
        EspecialidadeDto especialidadeSalva = especialidadeService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(especialidadeSalva, "Especialidade criada com sucesso."));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EspecialidadeDto>>> listarEspecialidades() {
        return ResponseEntity.ok(ApiResponse.success(
                especialidadeService.findAll(),
                "Especialidades recuperadas com sucesso."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EspecialidadeDto>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                especialidadeService.findById(id),
                "Especialidade recuperada com sucesso."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EspecialidadeDto>> atualizarEspecialidade(@PathVariable Long id,
                                                                                @Valid @RequestBody EspecialidadeRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                especialidadeService.update(id, request),
                "Especialidade atualizada com sucesso."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletarEspecialidade(@PathVariable Long id) {
        especialidadeService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Especialidade removida com sucesso."));
    }
}