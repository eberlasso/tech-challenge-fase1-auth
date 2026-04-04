package br.com.user.service.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for providing user information to the client.
 * This object excludes sensitive data such as passwords.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Public user profile information")
public class UserResponseDTO {

    /**
     * Unique identifier from the database.
     */
    @Schema(description = "User ID", example = "1")
    private Long id;

    /**
     * Full name of the user.
     */
    @Schema(description = "Full name", example = "Eber Silva")
    private String name;

    /**
     * Unique contact email.
     */
    @Schema(description = "Contact email", example = "eber@example.com")
    private String email;

    /**
     * Login handle used for authentication.
     */
    @Schema(description = "Login handle", example = "ebersilva")
    private String login;

    /**
     * Type of account (RESTAURANT_OWNER or CLIENT).
     */
    @Schema(description = "Account role", example = "RESTAURANT_OWNER")
    private String type;

    /**
     * User's physical address details.
     */
    @Schema(description = "Full address information")
    private AddressDTO address;

    /**
     * Timestamp of the last profile modification.
     */
    @Schema(description = "Date and time of the last update", example = "2026-04-03T20:44:00")
    private LocalDateTime lastUpdateDate;
}
