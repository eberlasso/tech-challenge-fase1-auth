package br.com.user.service.auth.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * Domain entity for user roles.
 * Allows dynamic management of permissions without code changes.
 */
@Entity
@Table(name = "tb_roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role extends AuditableEntity {
    /**
     * Unique identifier for the role.
     * Mapped to the 'ID' column in the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    /**
     * The unique name of the role used for authorization checks.
     * Example: 'CUSTOMER' or 'RESTAURANT_OWNER'.
     */
    @Column(unique = true, nullable = false, length = 255, name = "NAME")
    private String name;

    /**
     * A detailed description of what permissions this role grants.
     */
    @Column(nullable = false, length = 255, name = "DESCRIPTION")
    private String description;

    /**
     * Compares two Role objects based on their persistent identity (ID).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id);
    }

    /**
     * Generates a hash code based on the unique ID.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
