package br.com.user.service.auth.dto;

import br.com.user.service.auth.dto.validator.ValidUserType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for user registration with full documentation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User registration payload")
public class CreateUserRequestDTO {
    /**
     * Full name property for the new user.
     */
    @NotBlank(message = "Name cannot be empty")
    @Schema(description = "Full name", example = "João José da Silva")
    private String name;

    /**
     * Valid email for registration.
     */
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @Schema(description = "User email", example = "silva@example.com")
    private String email;

    /**
     * Chosen login handle.
     */
    @NotBlank(message = "Login cannot be empty")
    @Schema(description = "Login handle", example = "jjsilva")
    private String login;

    /**
     * Raw password to be hashed by the service.
     */
    @NotBlank(message = "Password cannot be empty")
    @Schema(description = "Secure password", example = "P@ssw0rd123")
    private String password;

    /**
     * Type of account (CLIENT or RESTAURANT_OWNER).
     */
    @ValidUserType
    @Schema(description = "Type of account", example = "'CL' to CLIENT or 'RO' to RESTAURANT_OWNER")
    private String type;

    /**
     * Nested address details.
     */
    @Schema(description = "User address details")
    private AddressDTO address;
}
