package tech.skullprogrammer.bguard.api.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import tech.skullprogrammer.bguard.api.dto.ErrorResponse;
import tech.skullprogrammer.bguard.domain.SkullException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private HttpServletRequest request;

    public GlobalExceptionHandler(HttpServletRequest request) {
        this.request = request;
    }

    @ExceptionHandler(SkullException.class)
    public ResponseEntity<ErrorResponse> handleException(SkullException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .status(e.getErrorType().getHttpStatus().value())
                .path(request.getRequestURI())
                .correlationId(UUID.randomUUID().toString())
                .error(e.getErrorType())
                .payload(e.getPayload())
                .build();
        return ResponseEntity.status(e.getErrorType().getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException e) {
        Map<String, @Nullable String> payload = e.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        SkullException.ErrorType errorType = SkullException.ErrorType.INVALID_DATA;
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .status(errorType.getHttpStatus().value())
                .path(request.getRequestURI())
                .correlationId(UUID.randomUUID().toString())
                .error(errorType)
                .payload(payload)
                .build();
        return ResponseEntity.status(errorType.getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentTypeMismatchException e) {
        Map<String, @Nullable String> payload = Map.of(e.getName(), e.getMessage());
        SkullException.ErrorType errorType = SkullException.ErrorType.INVALID_DATA;
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .status(errorType.getHttpStatus().value())
                .path(request.getRequestURI())
                .correlationId(UUID.randomUUID().toString())
                .error(errorType)
                .payload(payload)
                .build();
        return ResponseEntity.status(errorType.getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        SkullException.ErrorType errorType = SkullException.ErrorType.UNEXPECTED_ERROR;
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .status(errorType.getHttpStatus().value())
                .path(request.getRequestURI())
                .correlationId(UUID.randomUUID().toString())
                .error(errorType)
                .build();
        return ResponseEntity.status(errorType.getHttpStatus())
                .body(errorResponse);
    }
}
