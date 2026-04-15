package br.com.user.service.auth.dto;

import br.com.user.service.auth.dto.validator.ValidUserType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Data Transfer Object for updating an existing user's profile.
 * Contains only editable fields to prevent accidental modification of sensitive data.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for updating user profile information")
public class UpdateUserRequestDTO {

    @NotBlank(message = "Full name is required for update")
    @Schema(description = "Updated full name of the user", example = "José Silva dos Santos")
    private String name;

    /**
     * Valid email for registration.
     */
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @Schema(description = "User email", example = "silva@example.com")
    private String email;

    /**
     * Type of account (CLIENT or RESTAURANT_OWNER).
     */
    @ValidUserType
    @Schema(description = "Type of account", example = "'CL' to CLIENT or 'RO' to RESTAURANT_OWNER")
    private String type;

    /**
     * Optional address update. If provided, the system will update the user's current address.
     */
    @Valid
    @Schema(description = "Updated address information")
    private AddressDTO address;
}