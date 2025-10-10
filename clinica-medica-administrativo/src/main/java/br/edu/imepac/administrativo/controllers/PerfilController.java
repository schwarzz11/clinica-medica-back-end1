package br.edu.imepac.administrativo.controllers;

import br.edu.imepac.comum.dtos.perfil.PerfilDto;
import br.edu.imepac.comum.dtos.perfil.PerfilRequest;
import br.edu.imepac.comum.dtos.responses.ApiResponse;
import br.edu.imepac.comum.services.PerfilService;
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
@RequestMapping("/perfis")
@RequiredArgsConstructor
public class PerfilController {

    private final PerfilService perfilService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PerfilDto>>> getAllPerfis() {
        return ResponseEntity.ok(ApiResponse.success(
                perfilService.findAll(),
                "Perfis recuperados com sucesso."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PerfilDto>> getPerfilById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                perfilService.findById(id),
                "Perfil recuperado com sucesso."));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PerfilDto>> createPerfil(@Valid @RequestBody PerfilRequest request) {
        PerfilDto createdPerfil = perfilService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdPerfil, "Perfil criado com sucesso."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PerfilDto>> updatePerfil(@PathVariable Long id,
                                                                @Valid @RequestBody PerfilRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                perfilService.update(id, request),
                "Perfil atualizado com sucesso."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePerfil(@PathVariable Long id) {
        perfilService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Perfil removido com sucesso."));
    }
}
