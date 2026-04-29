package br.com.user.service.auth.config.security;

import br.com.user.service.auth.entities.Address;
import br.com.user.service.auth.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    private TokenService tokenService;
    private User user;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        ReflectionTestUtils.setField(tokenService, "secret", "test-secret");

        Address address = Address.builder()
                .id(1L)
                .street("Rua Teste")
                .number("12345")
                .city("Cidade Teste")
                .zipCode("12345000")
                .deleted(false)
                .build();
        user = User.builder()
                .id(1L)
                .name("Teste")
                .email("teste@teste.com")
                .login("testuser")
                .password("123")
                .type("CLIENT")
                .address(address)
                .lastUpdateDate(null)
                .deleted(false)
                .build();
    }

    @Test
    @DisplayName("Should generate a valid token")
    void shouldGenerateValidToken() {
        String token = tokenService.generateToken(user);
        
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Should validate a correct token and return subject")
    void shouldValidateTokenAndReturnSubject() {
        String token = tokenService.generateToken(user);
        String subject = tokenService.validateToken(token);
        
        assertEquals("testuser", subject);
    }

    @Test
    @DisplayName("Should return empty string for invalid token")
    void shouldReturnEmptyStringForInvalidToken() {
        String invalidToken = "invalid-token-string";
        String subject = tokenService.validateToken(invalidToken);
        
        assertEquals("", subject);
    }

    @Test
    @DisplayName("Should extract role from token successfully")
    void shouldExtractRoleFromToken() {
        String token = tokenService.generateToken(user);
        String role = tokenService.getRoleFromToken(token);
        
        assertEquals("CLIENT", role);
    }

    @Test
    @DisplayName("Should return null for role extraction with invalid token")
    void shouldReturnNullForRoleWithInvalidToken() {
        String role = tokenService.getRoleFromToken("invalid-token");
        
        assertNull(role);
    }
}
