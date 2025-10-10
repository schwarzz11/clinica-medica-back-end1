package br.edu.imepac.administrativo.controllers;

import br.edu.imepac.comum.dtos.funcionario.FuncionarioDto;
import br.edu.imepac.comum.dtos.funcionario.FuncionarioRequest;
import br.edu.imepac.comum.dtos.responses.ApiResponse;
import br.edu.imepac.comum.services.FuncionarioService;
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
@RequestMapping("/funcionarios")
@RequiredArgsConstructor
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @PostMapping
    public ResponseEntity<ApiResponse<FuncionarioDto>> criarFuncionario(@Valid @RequestBody FuncionarioRequest request) {
        FuncionarioDto funcionarioCriado = funcionarioService.adicionarFuncionario(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(funcionarioCriado, "Funcionário criado com sucesso."));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FuncionarioDto>>> listarFuncionarios() {
        return ResponseEntity.ok(ApiResponse.success(
                funcionarioService.listarFuncionarios(),
                "Funcionários recuperados com sucesso."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FuncionarioDto>> buscarFuncionarioPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                funcionarioService.buscarFuncionarioPorId(id),
                "Funcionário recuperado com sucesso."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FuncionarioDto>> atualizarFuncionario(@PathVariable Long id,
                                                                            @Valid @RequestBody FuncionarioRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                funcionarioService.atualizarFuncionario(id, request),
                "Funcionário atualizado com sucesso."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> removerFuncionario(@PathVariable Long id) {
        funcionarioService.removerFuncionario(id);
        return ResponseEntity.ok(ApiResponse.success("Funcionário removido com sucesso."));
    }
}
