package tech.skullprogrammer.bguard.domain;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
public class SkullException extends RuntimeException{

    public enum ErrorType {
        ENTITY_NOT_FOUND (HttpStatus.NOT_FOUND),
        CUSTOMER_NOT_FOUND (HttpStatus.NOT_FOUND),
        SUPPLY_POINT_NOT_FOUND (HttpStatus.NOT_FOUND),
        CUSTOMER_ALREADY_EXISTS (HttpStatus.CONFLICT),
        INVOICE_ALREADY_EXISTS (HttpStatus.CONFLICT),
        ENTITY_ALREADY_EXISTS (HttpStatus.CONFLICT),
        IMPORT_JOB_ALREADY_PROCESSED (HttpStatus.CONFLICT),
        UNCHANGED_DATA(HttpStatus.UNPROCESSABLE_CONTENT),
        INVALID_DATA (HttpStatus.UNPROCESSABLE_CONTENT),
        CSV_ERROR (HttpStatus.BAD_REQUEST),
        JWT_ERROR (HttpStatus.UNAUTHORIZED),
        UNAUTHORIZED (HttpStatus.UNAUTHORIZED),
        IMPORT_JOB_ERROR (HttpStatus.INTERNAL_SERVER_ERROR),
        UNEXPECTED_ERROR (HttpStatus.INTERNAL_SERVER_ERROR);

        private final HttpStatus status;

        ErrorType(HttpStatus status) {
            this.status = status;
        }
        public HttpStatus getHttpStatus() {
            return status;
        }
    }

    private final ErrorType errorType;
    private Map<String, String> payload;


    public SkullException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public SkullException(ErrorType errorType) {
        this.errorType = errorType;
    }

    public SkullException(ErrorType errorType, Map<String, String> payload) {
        this.errorType = errorType;
        this.payload = payload;
    }
}
