package br.com.user.service.auth.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * Entity representing a physical address.
 * Maps to 'tb_addresses' in the database.
 */
@Entity
@Table(name = "tb_addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address extends AuditableEntity {
    /** * Unique identifier for the address.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** * Street name or public place.
     */
    @Column(nullable = false)
    private String street;

    /** * Building or house number.
     */
    @Column(nullable = false)
    private String number;

    /** * City name.
     */
    @Column(nullable = false)
    private String city;

    /** * Postal code (ZIP code).
     * Mapped to 'zip_code' in PostgreSQL.
     */
    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    /** * State or province.
     */
    @Column(nullable = false)
    private String state;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(id, address.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
