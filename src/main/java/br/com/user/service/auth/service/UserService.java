package br.com.user.service.auth.service;

import br.com.user.service.auth.dto.UserRegistrationDTO;
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
    public UserResponseDTO register(UserRegistrationDTO dto) {
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
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}