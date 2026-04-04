package br.com.user.service.auth.controller.user;


import br.com.user.service.auth.dto.UserRegistrationDTO;
import br.com.user.service.auth.dto.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for User Management with logging capabilities.
 */
@Slf4j
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Management")
public class UserController {

    /**
     * Registers a new user.
     */
    @PostMapping
    @Operation(summary = "Register a new user")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        log.info("Receiving request to register user with email: {}", registrationDTO.getEmail());

        try {
            // Lógica de serviço virá aqui
            log.info("User successfully registered: {}", registrationDTO.getLogin());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            log.error("Error during user registration: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Search users by name.
     */
    @GetMapping("/search")
    @Operation(summary = "Search users by name")
    public ResponseEntity<List<UserResponseDTO>> searchByName(@RequestParam String name) {
        log.debug("Searching for users with name containing: {}", name);
        return ResponseEntity.ok().build();
    }
}
