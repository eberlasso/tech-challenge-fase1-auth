package br.com.user.service.auth.entities;

import br.com.user.service.auth.enums.UserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Main entity representing a user in the database.
 * Each property includes JavaDoc, explicit column naming, and nullability constraints.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * Unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Full name of the user.
     */
    @Column(name = "full_name", nullable = false)
    private String name;

    /**
     * Unique email address with validation pattern.
     */
    @Email(message = "Invalid email format")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * Username handle for authentication purposes.
     */
    @Column(name = "login_handle", nullable = false, unique = true)
    private String login;

    /**
     * Encrypted password string.
     */
    @Column(name = "password_hash", nullable = false)
    private String password;

    /**
     * Defines if the user is a CLIENT or a RESTAURANT_OWNER.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private UserType type;

    /**
     * Composition of address-related fields.
     */
    @Embedded
    private Address address;

    /**
     * Timestamp of the most recent record modification.
     */
    @UpdateTimestamp
    @Column(name = "last_modification_date", nullable = false)
    private LocalDateTime lastUpdateDate;
}
