package br.com.user.service.auth.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
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
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id = ?") // Automatiza o delete
@SQLRestriction("deleted = false") // Filtra automaticamente nos selects
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
    @Column(name = "login_handle", nullable = false)
    private String login;

    /**
     * Encrypted password string.
     */
    @Column(name = "password_hash", nullable = false)
    private String password;

    /**
     * Defines if the user is a CLIENT or a RESTAURANT_OWNER.
     */
    @Column(name = "user_role", nullable = false)
    private String type;

    /**
     * One-to-one relationship with Address.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    /**
     * Timestamp of the most recent record modification.
     */
    @UpdateTimestamp
    @Column(name = "last_modification_date", nullable = false)
    private LocalDateTime lastUpdateDate;

    /**
     * Logical deletion flag (Soft Delete).
     * If true, the record is considered inactive and ignored by standard queries.
     */
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;
}
