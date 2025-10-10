package br.edu.imepac.administrativo.exceptions;

import br.edu.imepac.comum.dtos.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ClinicaMedicaHandleExceptions {

    @ExceptionHandler(AuthenticationClinicaMedicaException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnauthorized(AuthenticationClinicaMedicaException ex) {
        log.error("Erro de autenticação", ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Dados de acesso inválidos."));
    }
}