package br.com.user.service.auth.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class SecurityErrorHandlersTest {

    private CustomAuthenticationEntryPoint authenticationEntryPoint;
    private CustomAccessDeniedHandler accessDeniedHandler;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws IOException {
        authenticationEntryPoint = new CustomAuthenticationEntryPoint();
        accessDeniedHandler = new CustomAccessDeniedHandler();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    @Test
    @DisplayName("Should return 401 JSON when authentication entry point is called")
    void shouldReturn401Json() throws IOException {
        AuthenticationException authEx = mock(AuthenticationException.class);
        
        authenticationEntryPoint.commence(request, response, authEx);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/problem+json");
        String result = responseWriter.toString();
        assertTrue(result.contains("Unauthorized"));
        assertTrue(result.contains("401"));
    }

    @Test
    @DisplayName("Should return 403 JSON when access denied handler is called")
    void shouldReturn403Json() throws IOException {
        AccessDeniedException accessEx = mock(AccessDeniedException.class);

        accessDeniedHandler.handle(request, response, accessEx);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(response).setContentType("application/problem+json");
        String result = responseWriter.toString();
        assertTrue(result.contains("Access Denied"));
        assertTrue(result.contains("403"));
    }
}
