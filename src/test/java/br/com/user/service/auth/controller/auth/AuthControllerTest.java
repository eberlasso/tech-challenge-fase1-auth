package br.com.user.service.auth.controller.auth;

import br.com.user.service.auth.config.security.TokenService;
import br.com.user.service.auth.dto.LoginRequestDTO;
import br.com.user.service.auth.dto.LoginResponseDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private TokenService tokenService;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthController authController;

    private LoginRequestDTO loginRequestDTO;
    private User user;

    @BeforeEach
    void setUp() {
        // Atualizado para incluir o email no LoginRequestDTO
        loginRequestDTO = new LoginRequestDTO("testuser", "password", "teste@teste.com");
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
    @DisplayName("Should login successfully and return token")
    void shouldLoginSuccessfully() {
        Authentication auth = mock(Authentication.class);
        // O token de autenticação agora usa o username combinado
        UsernamePasswordAuthenticationToken expectedAuthToken = new UsernamePasswordAuthenticationToken(
                loginRequestDTO.login() + "::" + loginRequestDTO.email(), loginRequestDTO.password());

        when(authenticationManager.authenticate(eq(expectedAuthToken))).thenReturn(auth);
        // Busca o usuário usando findByLoginAndEmail
        when(userRepository.findByLoginAndEmail(loginRequestDTO.login(), loginRequestDTO.email())).thenReturn(Optional.of(user));
        when(tokenService.generateToken(user)).thenReturn("test-token");

        ResponseEntity<LoginResponseDTO> response = authController.login(loginRequestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("test-token", response.getBody().token());
        
        verify(authenticationManager, times(1)).authenticate(eq(expectedAuthToken));
        verify(userRepository, times(1)).findByLoginAndEmail(loginRequestDTO.login(), loginRequestDTO.email());
        verify(tokenService, times(1)).generateToken(user);
    }

    @Test
    @DisplayName("Should throw exception when user not found after authentication with login and email")
    void shouldThrowExceptionWhenUserNotFoundAfterAuth() {
        Authentication auth = mock(Authentication.class);
        UsernamePasswordAuthenticationToken expectedAuthToken = new UsernamePasswordAuthenticationToken(
                loginRequestDTO.login() + "::" + loginRequestDTO.email(), loginRequestDTO.password());

        when(authenticationManager.authenticate(eq(expectedAuthToken))).thenReturn(auth);
        // Simula que o usuário não foi encontrado por login e email
        when(userRepository.findByLoginAndEmail(loginRequestDTO.login(), loginRequestDTO.email())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
                authController.login(loginRequestDTO));

        assertEquals("User not found after authentication with login and email", exception.getMessage());
        verify(authenticationManager, times(1)).authenticate(eq(expectedAuthToken));
        verify(userRepository, times(1)).findByLoginAndEmail(loginRequestDTO.login(), loginRequestDTO.email());
        verify(tokenService, never()).generateToken(any(User.class)); // Token não deve ser gerado
    }
}
