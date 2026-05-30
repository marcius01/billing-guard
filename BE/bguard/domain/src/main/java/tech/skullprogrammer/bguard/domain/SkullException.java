package tech.skullprogrammer.bguard.domain;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SkullException extends RuntimeException{

    public enum ErrorType {
        ENTITY_NOT_FOUND (HttpStatus.NOT_FOUND),
        CUSTOMER_NOT_FOUND (HttpStatus.NOT_FOUND),
        CUSTOMER_ALREADY_EXISTS (HttpStatus.CONFLICT),
        ENTITY_ALREADY_EXISTS (HttpStatus.CONFLICT),
        INVALID_DATA (HttpStatus.UNPROCESSABLE_CONTENT),
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


    public SkullException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public SkullException(ErrorType errorType) {
        this.errorType = errorType;
    }
}
