package br.com.user.service.auth.service;

import br.com.user.service.auth.entities.Address;
import br.com.user.service.auth.entities.User;
import br.com.user.service.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthorizationService authorizationService;

    private User user;

    @BeforeEach
    void setUp() {
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
                .password("encodedPassword") // Usar uma senha codificada para simular o comportamento real
                .type("CLIENT")
                .address(address)
                .lastUpdateDate(null)
                .deleted(false)
                .build();
    }

    @Test
    @DisplayName("Should load user by combined username (login::email) successfully")
    void shouldLoadUserByCombinedUsernameSuccessfully() {
        String combinedUsername = "testuser::teste@teste.com";
        when(userRepository.findByLoginAndEmail("testuser", "teste@teste.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = authorizationService.loadUserByUsername(combinedUsername);

        assertNotNull(userDetails);
        assertEquals(user.getEmail(), userDetails.getUsername()); // O username do UserDetails agora é o email
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CLIENT")));
        
        verify(userRepository, times(1)).findByLoginAndEmail("testuser", "teste@teste.com");
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user not found by combined username")
    void shouldThrowExceptionWhenUserNotFoundByCombinedUsername() {
        String combinedUsername = "nonexistent::nonexistent@example.com";
        when(userRepository.findByLoginAndEmail(anyString(), anyString())).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> 
                authorizationService.loadUserByUsername(combinedUsername));
        
        assertEquals("User not found with login: nonexistent and email: nonexistent@example.com", exception.getMessage());
        verify(userRepository, times(1)).findByLoginAndEmail("nonexistent", "nonexistent@example.com");
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when combined username format is invalid")
    void shouldThrowExceptionWhenCombinedUsernameFormatIsInvalid() {
        String invalidUsername = "testuser_only"; // Falta o "::email"

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> 
                authorizationService.loadUserByUsername(invalidUsername));
        
        assertEquals("Invalid username format. Expected 'login::email'.", exception.getMessage());
        verify(userRepository, never()).findByLoginAndEmail(anyString(), anyString());
    }
}
