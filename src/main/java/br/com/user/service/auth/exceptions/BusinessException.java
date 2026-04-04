package br.com.user.service.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception for business rule violations.
 * This exception is caught by the GlobalExceptionHandler to return a ProblemDetail (RFC 7807).
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusinessException extends RuntimeException {

    /**
     * Constructs a new BusinessException with the specified detail message.
     *
     * @param message the detail message.
     */
    public BusinessException(String message) {
        super(message);
    }

    /**
     * Constructs a new BusinessException with message and cause.
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}