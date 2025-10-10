package br.edu.imepac.administrativo.controllers;

import br.edu.imepac.comum.dtos.convenio.ConvenioDto;
import br.edu.imepac.comum.dtos.convenio.ConvenioRequest;
import br.edu.imepac.comum.dtos.responses.ApiResponse;
import br.edu.imepac.comum.services.ConvenioService;
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
@RequestMapping("/convenios")
@RequiredArgsConstructor
public class ConvenioController {

    private final ConvenioService convenioService;

    @PostMapping
    public ResponseEntity<ApiResponse<ConvenioDto>> save(@Valid @RequestBody ConvenioRequest convenioRequest) {
        ConvenioDto novoConvenio = convenioService.save(convenioRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(novoConvenio, "Convênio criado com sucesso."));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ConvenioDto>>> listAll() {
        return ResponseEntity.ok(ApiResponse.success(
                convenioService.findAll(),
                "Convênios recuperados com sucesso."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ConvenioDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                convenioService.findById(id),
                "Convênio recuperado com sucesso."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ConvenioDto>> update(@PathVariable Long id,
                                                           @Valid @RequestBody ConvenioRequest convenioRequest) {
        return ResponseEntity.ok(ApiResponse.success(
                convenioService.update(id, convenioRequest),
                "Convênio atualizado com sucesso."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        convenioService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Convênio removido com sucesso."));
    }
}
