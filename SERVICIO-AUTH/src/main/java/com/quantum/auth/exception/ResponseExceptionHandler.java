package com.quantum.auth.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.stream.Collectors;

import java.time.LocalDateTime;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleAllException(Exception ex, WebRequest request) {
        // CAMBIAR ex.getMessage() en producción
        ExceptionResponse er = new ExceptionResponse(LocalDateTime.now(), ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(er, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ModelNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleModelNotFoundException(ModelNotFoundException ex,
            WebRequest request) {
        // CAMBIAR ex.getMessage() en producción
        ExceptionResponse er = new ExceptionResponse(LocalDateTime.now(), ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(er, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {

        // Filtrar los mensajes para que solo se usen los personalizados (no los
        // predeterminados como "must not be empty")
        String msg = ex.getBindingResult().getAllErrors().stream()
                .map(e -> {
                    String defaultMessage = e.getDefaultMessage();
                    // Solo incluir mensajes personalizados y filtrar cualquier mensaje genérico no
                    // deseado
                    return (defaultMessage != null)
                            ? defaultMessage.concat(",")
                            : "";
                })
                .collect(Collectors.joining());

        ExceptionResponse er = new ExceptionResponse(LocalDateTime.now(), msg, request.getDescription(false));

        return new ResponseEntity<>(er, HttpStatus.BAD_REQUEST);
    }

}