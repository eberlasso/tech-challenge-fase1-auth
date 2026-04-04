package br.com.user.service.auth.repository;

import br.com.user.service.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity persistence operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds users by name containing a specific string (Case Insensitive).
     * Requirement: "Busca de usuários pelo nome".
     */
    List<User> findByNameContainingIgnoreCase(String name);

    /**
     * Finds a user by their unique email.
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds a user by their unique login handle.
     */
    Optional<User> findByLogin(String login);
}
