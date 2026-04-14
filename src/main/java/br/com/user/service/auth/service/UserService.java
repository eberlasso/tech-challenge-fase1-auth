package br.com.user.service.auth.service;

import br.com.user.service.auth.dto.CreateUserRequestDTO;
import br.com.user.service.auth.dto.UserResponseDTO;
import br.com.user.service.auth.entities.User;
import br.com.user.service.auth.exceptions.BusinessException;
import br.com.user.service.auth.mapper.UserMapper;
import br.com.user.service.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class responsible for handling user-related business logic.
 * This includes user registration, password encryption, and profile searches.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Registers a new user in the system after validating email uniqueness.
     * The password is encrypted using BCrypt before persistence.
     *
     * @param dto Data Transfer Object containing registration details.
     * @return UserResponseDTO containing the persisted user information.
     * @throws BusinessException if the email is already registered in the database.
     */
    @Transactional
    public UserResponseDTO create(CreateUserRequestDTO dto) {
        log.info("Starting registration for: {}", dto.getEmail());

        // Business Rule: Check if email is unique
        userRepository.findByEmail(dto.getEmail()).ifPresent(user -> {
            log.error("Registration failed: Email {} already exists", dto.getEmail());
            throw new BusinessException("The email address is already registered in our system.");
        });

        // Map DTO to Entity using MapStruct
        User user = userMapper.toEntity(dto);

        // Securely encode the password before saving
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Persist to PostgreSQL
        User savedUser = userRepository.save(user);
        log.info("User registered with ID: {}", savedUser.getId());

        return userMapper.toResponseDTO(savedUser);
    }

    /**
     * Retrieves a list of users whose full names contain the specified string.
     * This operation is optimized for read-only access.
     *
     * @param name The string to search for within user names.
     * @return A list of UserResponseDTO matching the criteria.
     */
    @Transactional(readOnly = true)
    public List<UserResponseDTO> findByName(String name) {
        log.debug("Searching for users with name containing: {}", name);
        return userRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(userMapper::toResponseDTO).toList();
    }
    /**
     * Updates an existing user's profile and address information.
     * Uses MapStruct to safely merge DTO changes into the existing managed entity.
     *
     * @param id  The unique identifier of the user to update.
     * @param dto DTO containing the updated information (name, login, address).
     * @return UserResponseDTO with the updated state.
     * @throws BusinessException if the user is not found.
     */
    @Transactional
    public UserResponseDTO update(Long id, UpdateUserRequestDTO dto) {
        log.info("Updating user profile for ID: {}", id);

        // 1. Busca a entidade no banco (garante que ela existe)
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Update failed: User ID {} not found", id);
                    return new BusinessException("User not found with the provided ID.");
                });

        // 2. O MapStruct mapeia as alterações do DTO para a entidade existente
        // Isso inclui o mapeamento de campos simples (name, login)
        userMapper.updateUserFromDto(dto, existingUser);

        // 3. Atualização do Endereço (Lógica de Relacionamento)
        // Se o DTO trouxe dados de endereço e o usuário já possui um endereço,
        // usamos o mapper específico para atualizar a instância de Address existente.
        if (dto.getAddress() != null && existingUser.getAddress() != null) {
            userMapper.updateAddressFromDto(dto.getAddress(), existingUser.getAddress());
        }

        // 4. Salva as alterações (O Hibernate cuidará do Dirty Checking)
        User updatedUser = userRepository.save(existingUser);
        log.info("User ID {} updated successfully", id);

        return userMapper.toResponseDTO(updatedUser);
    }

    /**
     * Performs a logical deletion of the user.
     * The record's 'deleted' flag is set to true, making it inactive for standard queries.
     *
     * @param id The unique identifier of the user to be deactivated.
     * @throws BusinessException if the user is not found.
     */
    @Transactional
    public void delete(Long id) {
        log.info("Attempting logical deletion for user ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Deletion failed: User ID {} not found", id);
                    return new BusinessException("User not found with the provided ID.");
                });

        // Soft Delete: Hibernate @SQLDelete could also handle this,
        // but explicit setting is safer for business traceability.
        user.setDeleted(true);

        // Se o endereço deve ser inativado junto:
        if (user.getAddress() != null) {
            user.getAddress().setDeleted(true);
        }

        userRepository.save(user);
        log.info("User ID {} marked as deleted (inactive)", id);
    }

    /**
     * Retrieves all active users in the system.
     * Note: Deleted users are automatically filtered if @SQLRestriction is active on the entity.
     *
     * @return List of all active UserResponseDTOs.
     */
    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        log.debug("Listing all active users");
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponseDTO)
                .toList();
    }
}