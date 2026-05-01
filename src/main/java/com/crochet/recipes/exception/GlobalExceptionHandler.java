package com.crochet.recipes.exception;

import com.crochet.recipes.dto.response.ApiResponseDTO;
import com.crochet.recipes.dto.error.ErrorDetailsDTO;
import com.crochet.recipes.dto.error.ValidationErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecipeNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<ErrorDetailsDTO>> handleRecipeNotFound(
            RecipeNotFoundException ex, HttpServletRequest request) {

        String traceId = UUID.randomUUID().toString();
        log.warn("[{}] Receita não encontrada: {}", traceId, ex.getMessage());

        ErrorDetailsDTO details = ErrorDetailsDTO.builder()
            .code("RECIPE_NOT_FOUND")
            .message(ex.getMessage())
            .httpStatus(404)
            .timestamp(LocalDateTime.now())
            .traceId(traceId)
            .path(request.getRequestURI())
            .build();

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ApiResponseDTO.error(details));
    }

    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<ApiResponseDTO<ErrorDetailsDTO>> handleJwtAuthenticationException(
            JwtAuthenticationException ex, HttpServletRequest request) {

        String traceId = UUID.randomUUID().toString();
        log.warn("[{}] Erro de autenticação JWT: {}", traceId, ex.getMessage());

        ErrorDetailsDTO details = ErrorDetailsDTO.builder()
            .code("JWT_AUTHENTICATION_ERROR")
            .message(ex.getMessage())
            .httpStatus(401)
            .timestamp(LocalDateTime.now())
            .traceId(traceId)
            .path(request.getRequestURI())
            .build();

        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ApiResponseDTO.error(details));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<ErrorDetailsDTO>> handleUserNotFound(
            UserNotFoundException ex, HttpServletRequest request) {

        String traceId = UUID.randomUUID().toString();
        log.warn("[{}] Usuário não encontrado: {}", traceId, ex.getMessage());

        ErrorDetailsDTO details = ErrorDetailsDTO.builder()
            .code("USER_NOT_FOUND")
            .message(ex.getMessage())
            .httpStatus(404)
            .timestamp(LocalDateTime.now())
            .traceId(traceId)
            .path(request.getRequestURI())
            .build();

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ApiResponseDTO.error(details));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<ValidationErrorDTO>> handleValidationErrors(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        String traceId = UUID.randomUUID().toString();
        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });

        log.warn("[{}] Erros de validação: {}", traceId, fieldErrors);

        ValidationErrorDTO details = ValidationErrorDTO.builder()
            .code("VALIDATION_ERROR")
            .message("Erro de validação nos dados enviados")
            .httpStatus(400)
            .timestamp(LocalDateTime.now())
            .traceId(traceId)
            .path(request.getRequestURI())
            .fieldErrors(fieldErrors)
            .build();

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponseDTO.error(details));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<ErrorDetailsDTO>> handleGenericException(
            Exception ex, HttpServletRequest request) {

        String traceId = UUID.randomUUID().toString();
        log.error("[{}] Erro inesperado em {}: {}", traceId, request.getRequestURI(), ex.getMessage(), ex);

        ErrorDetailsDTO details = ErrorDetailsDTO.builder()
            .code("INTERNAL_ERROR")
            .message("Erro interno do servidor")
            .httpStatus(500)
            .timestamp(LocalDateTime.now())
            .traceId(traceId)
            .path(request.getRequestURI())
            .hint("Entre em contato com suporte informando traceId: " + traceId)
            .build();

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponseDTO.error(details));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponseDTO<ErrorDetailsDTO>> handleUserAlreadyExists(
            UserAlreadyExistsException ex, HttpServletRequest request) {

        String traceId = UUID.randomUUID().toString();
        log.warn("[{}] Tentativa de criar usuário duplicado: {}", traceId, ex.getMessage());

        ErrorDetailsDTO details = ErrorDetailsDTO.builder()
            .code("USER_ALREADY_EXISTS")
            .message(ex.getMessage())
            .httpStatus(409)
            .timestamp(LocalDateTime.now())
            .traceId(traceId)
            .path(request.getRequestURI())
            .build();

        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ApiResponseDTO.error(details));
    }
}
