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
        log.info("Attempting login for user: {}", loginRequest.login());
        
        var usernamePassword = new UsernamePasswordAuthenticationToken(loginRequest.login(), loginRequest.password());
        
        this.authenticationManager.authenticate(usernamePassword);

        var user = userRepository.findByLogin(loginRequest.login())
                .orElseThrow(() -> new RuntimeException("User not found after authentication"));

        var token = tokenService.generateToken(user);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}
