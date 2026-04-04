package br.com.user.service.auth.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

/**
 * Embedded address entity containing location details.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {

    /**
     * Name of the street or public way.
     */
    @Column(name = "address_street", nullable = false)
    private String street;

    /**
     * Building or house number.
     */
    @Column(name = "address_number", nullable = false)
    private String number;

    /**
     * City name.
     */
    @Column(name = "address_city", nullable = false)
    private String city;

    /**
     * Postal or Zip code.
     */
    @Column(name = "address_zip_code", nullable = false)
    private String zipCode;
}