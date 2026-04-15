package br.com.user.service.auth.controller.user;


import br.com.user.service.auth.domain.GeneralResponse;
import br.com.user.service.auth.dto.CreateUserRequestDTO;
import br.com.user.service.auth.dto.UpdatePasswordRequestDTO;
import br.com.user.service.auth.dto.UpdateUserRequestDTO;
import br.com.user.service.auth.dto.UserResponseDTO;
import br.com.user.service.auth.service.UserService;
import br.com.user.service.auth.utils.GeneralConstants;
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
    private final UserService userService;

    /**
     * Registers a new user.
     */
    @PostMapping
    @Operation(summary = "Register a new user")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequestDTO registrationDTO) {
        log.info("Receiving request to register user with email: {}", registrationDTO.getEmail());
        userService.create(registrationDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(new GeneralResponse(1, GeneralConstants.SUCCESS_CREATE_USER, true));
    }

    /**
     * Updates an existing user's information.
     * This follows the PUT semantic (full replacement of editable fields).
     *
     * @param id The unique identifier of the user.
     * @param dto The updated user data.
     * @return The updated user data and HTTP status 200.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing user", description = "Updates fields like name and login for a specific user ID.")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody UpdateUserRequestDTO dto) {
        userService.update(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(new GeneralResponse(2, GeneralConstants.UPDATED_CREATE_USER, true));
    }

    /**
     * Performs a logical deletion of a user.
     * The record remains in the database with the 'deleted' flag set to true.
     *
     * @param id The unique identifier of the user to be deleted.
     * @return HTTP status 204 (No Content) on success.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user (Soft Delete)", description = "Marks a user as inactive without removing the record from the database.")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
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

    /**
     * Endpoint dedicado para operações de segurança do usuário.
     * * @param id ID do usuário extraído da URL.
     * @param dto Dados de validação e nova senha.
     * @return ResponseEntity com status 204 (No Content) em caso de sucesso.
     */
    @PatchMapping("/{id}/password")
    @Operation(
            summary = "Update user password",
            description = "Exclusive endpoint for password change, separate from profile update.."
    )
    public ResponseEntity<Void> updatePassword(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePasswordRequestDTO dto) {

        userService.updatePassword(id, dto);
        return ResponseEntity.noContent().build();
    }


}
