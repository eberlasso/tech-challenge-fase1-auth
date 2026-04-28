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
                .password("123")
                .type("CLIENT")
                .address(address)
                .lastUpdateDate(null)
                .deleted(false)
                .build();
    }

    @Test
    @DisplayName("Should load user by username successfully")
    void shouldLoadUserByUsernameSuccessfully() {
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(user));

        UserDetails userDetails = authorizationService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CLIENT")));
        
        verify(userRepository, times(1)).findByLogin("testuser");
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user not found")
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> 
                authorizationService.loadUserByUsername("nonexistent"));
        
        verify(userRepository, times(1)).findByLogin("nonexistent");
    }
}
