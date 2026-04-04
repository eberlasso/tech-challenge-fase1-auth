package br.com.user.service.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for address information.
 * Used to receive and send address data through the API.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Information about the user's physical location")
public class AddressDTO {
        /**
         * The name of the street or public thoroughfare.
         */
        @NotBlank(message = "Street is required")
        @Schema(description = "Street name", example = "Avenida Rudge Ramos")
        private String street;

        /**
         * The specific number of the building or house.
         */
        @NotBlank(message = "Number is required")
        @Schema(description = "Building number", example = "123")
        private String number;

        /**
         * The city where the user is located.
         */
        @NotBlank(message = "City is required")
        @Schema(description = "City name", example = "São Bernardo do Campo")
        private String city;

        /**
         * The postal code or ZIP code.
         */
        @NotBlank(message = "Zip code is required")
        @Schema(description = "Postal code", example = "09630-000")
        private String zipCode;

}
