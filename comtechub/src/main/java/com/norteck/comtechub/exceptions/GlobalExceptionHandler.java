package com.norteck.comtechub.exceptions;

import com.norteck.comtechub.dto.response.ExceptionResponseDTO;
import com.norteck.comtechub.exceptions.custom.ConflictException;
import com.norteck.comtechub.exceptions.custom.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> handleConflictException(ConflictException ex) {
        ExceptionResponseDTO erro = new ExceptionResponseDTO(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Já existente.",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex) {
        ExceptionResponseDTO erro = new ExceptionResponseDTO(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Não encontrado",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }
}
