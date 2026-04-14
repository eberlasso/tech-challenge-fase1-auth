package br.com.user.service.auth.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

/**
 * Entity representing an address in the database.
 * Now a separate entity with a 1-to-1 relationship to User.
 */
@Entity
@Table(name = "addresses")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id = ?") // Automatiza o delete
@SQLRestriction("deleted = false") // Filtra automaticamente nos selects
public class Address {

    /**
     * Unique identifier for the address.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Name of the street or public way.
     */
    @Column(name = "street", nullable = false)
    private String street;

    /**
     * Building or house number.
     */
    @Column(name = "number", nullable = false)
    private String number;

    /**
     * City name.
     */
    @Column(name = "city", nullable = false)
    private String city;

    /**
     * Postal or Zip code.
     */
    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    /**
     * Logical deletion flag (Soft Delete).
     * If true, the record is considered inactive and ignored by standard queries.
     */
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;
}
