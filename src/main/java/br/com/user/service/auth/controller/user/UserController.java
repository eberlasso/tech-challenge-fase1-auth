package br.com.user.service.auth.controller.user;

import br.com.user.service.auth.domain.GeneralResponse;
import br.com.user.service.auth.dto.CreateUserRequestDTO;
import br.com.user.service.auth.dto.UpdatePasswordRequestDTO;
import br.com.user.service.auth.dto.UpdateUserRequestDTO;
import br.com.user.service.auth.dto.UserResponseDTO;
import br.com.user.service.auth.service.UserService;
import br.com.user.service.auth.utils.GeneralConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for User Management.
 */
@Slf4j
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequestDTO registrationDTO) {
        log.info("Receiving request to register user with email: {}", registrationDTO.getEmail());
        userService.create(registrationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new GeneralResponse(1, GeneralConstants.SUCCESS_CREATE_USER, true));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody UpdateUserRequestDTO dto) {
        userService.update(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(new GeneralResponse(2, GeneralConstants.UPDATED_CREATE_USER, true));
    }

    /**
     * Only Restaurant Owners (RO) can delete users.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('RO')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<UserResponseDTO>> searchByName(
            @RequestParam(required = false) String name,
            @PageableDefault(size = 10) Pageable pageable) {
        log.debug("Searching for users with name containing: {} and pagination: {}", name, pageable);
        return ResponseEntity.ok(userService.findByName(name, pageable));
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePasswordRequestDTO dto) {
        userService.updatePassword(id, dto);
        return ResponseEntity.noContent().build();
    }
}
