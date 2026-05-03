package br.com.user.service.auth.controller.auth;

import br.com.user.service.auth.config.security.TokenService;
import br.com.user.service.auth.dto.LoginRequestDTO;
import br.com.user.service.auth.dto.LoginResponseDTO;
import br.com.user.service.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller dedicated to authentication operations.
 * Documentation is handled via static openapi.yaml.
 */
@Slf4j
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        log.info("Attempting login for user: {} with email: {}", loginRequest.login(), loginRequest.email());
        
        // Concatena login e email para passar como "username" para o Spring Security
        String combinedUsername = loginRequest.login() + "::" + loginRequest.email();
        
        var usernamePassword = new UsernamePasswordAuthenticationToken(combinedUsername, loginRequest.password());
        
        this.authenticationManager.authenticate(usernamePassword);

        // Após autenticação bem-sucedida, busca o usuário completo usando login e email
        var user = userRepository.findByLoginAndEmail(loginRequest.login(), loginRequest.email())
                .orElseThrow(() -> new RuntimeException("User not found after authentication with login and email"));

        var token = tokenService.generateToken(user);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}
