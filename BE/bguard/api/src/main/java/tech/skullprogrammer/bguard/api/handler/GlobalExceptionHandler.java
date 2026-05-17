package tech.skullprogrammer.bguard.api.handler;

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

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SkullException.class)
    public ResponseEntity<ErrorResponse> handleException(SkullException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .error(e.getErrorType())
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
                .error(errorType)
                .payload(payload)
                .build();
        return ResponseEntity.status(errorType.getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .error(SkullException.ErrorType.UNEXPECTED_ERROR)
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}
