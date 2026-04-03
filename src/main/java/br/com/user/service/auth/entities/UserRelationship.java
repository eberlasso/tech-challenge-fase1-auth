package br.com.user.service.auth.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Main entity for authentication and authorization management.
 * This class implements the Identity and Access Management (IAM) strategy
 * by linking a physical Identity (Person) to a specific Role and Address.
 * * It acts as the bridge between the user's credentials and their
 * permissions within the restaurant system.
 * * Maps to 'tb_user_relationships' in the PostgreSQL database.
 */
@Entity
@Table(name = "tb_user_relationships")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRelationship extends AuditableEntity {

    /**
     * Unique identifier for the relationship record.
     * Uses Identity strategy for PostgreSQL serial/bigserial compatibility.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The physical or legal person associated with this account.
     * Uses Lazy fetching to optimize performance when only auth data is needed.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    /**
     * The specific address associated with this user role.
     * Allows a single person to have different addresses for different roles.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    /**
     * The domain role assigned to this relationship (e.g., CUSTOMER or OWNER).
     * Uses Eager fetching because roles are mandatory for authorization checks.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    /**
     * Unique login identifier (username or email) used for authentication.
     */
    @Column(nullable = false, unique = true)
    private String login;

    /**
     * Hashed password for security.
     * This field stores the BCrypt hash, never the plain text password.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Compares two UserRelationship objects based on their ID.
     * Ensures consistent behavior across persistence contexts.
     * * @param o The object to compare with.
     * @return true if IDs are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRelationship other)) return false;
        return id != null && id.equals(other.getId());
    }

    /**
     * Returns a hash code value for the entity.
     * Uses the class hash code to maintain consistency with JPA proxies.
     * * @return the hash code value.
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}