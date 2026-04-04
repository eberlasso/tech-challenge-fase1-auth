package br.com.user.service.auth.mapper;

import br.com.user.service.auth.dto.UserRegistrationDTO;
import br.com.user.service.auth.dto.UserResponseDTO;
import br.com.user.service.auth.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between User entities and DTOs.
 * Uses MapStruct for high-performance compile-time code generation.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Maps Registration DTO to User Entity.
     * Password encryption is handled externally or via a decorator if needed,
     * but here we map the basic fields.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    @Mapping(target = "password", ignore = true) // Handled in Service for security clarity
    User toEntity(UserRegistrationDTO dto);

    /**
     * Maps User Entity to Response DTO for API output.
     */
    UserResponseDTO toResponseDTO(User user);
}