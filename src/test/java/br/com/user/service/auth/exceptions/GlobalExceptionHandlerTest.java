package br.com.user.service.auth.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("Should handle BusinessException")
    void shouldHandleBusinessException() {
        BusinessException ex = new BusinessException("Business error message");
        ProblemDetail result = exceptionHandler.handleBusinessException(ex);

        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
        assertEquals("Business error message", result.getDetail());
        assertEquals("Business Rule Violation", result.getTitle());
    }

    @Test
    @DisplayName("Should handle Validation Exception")
    void shouldHandleValidationException() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "field", "message");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ProblemDetail result = exceptionHandler.handleValidationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
        assertEquals("Validation Failed", result.getTitle());
        assertNotNull(result.getProperties().get("invalid_params"));
    }

    @Test
    @DisplayName("Should handle BadCredentialsException")
    void shouldHandleBadCredentialsException() {
        BadCredentialsException ex = new BadCredentialsException("Bad credentials");
        ProblemDetail result = exceptionHandler.handleBadCredentialsException(ex);

        assertEquals(HttpStatus.UNAUTHORIZED.value(), result.getStatus());
        assertEquals("Authentication Failed", result.getTitle());
    }

    @Test
    @DisplayName("Should handle UserNotFoundException")
    void shouldHandleUserNotFoundException() {
        UserNotFoundException ex = new UserNotFoundException("User not found");
        ProblemDetail result = exceptionHandler.handleUserNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND.value(), result.getStatus());
        assertEquals("User Not Found", result.getTitle());
    }

    @Test
    @DisplayName("Should handle General Exception")
    void shouldHandleGeneralException() {
        Exception ex = new Exception("General error");
        ProblemDetail result = exceptionHandler.handleGeneralException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getStatus());
        assertEquals("Internal Server Error", result.getTitle());
    }
}
