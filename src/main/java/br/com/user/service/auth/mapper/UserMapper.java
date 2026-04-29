package br.com.user.service.auth.mapper;

import br.com.user.service.auth.dto.AddressDTO;
import br.com.user.service.auth.dto.CreateUserRequestDTO;
import br.com.user.service.auth.dto.UpdateUserRequestDTO;
import br.com.user.service.auth.dto.UserResponseDTO;
import br.com.user.service.auth.entities.Address;
import br.com.user.service.auth.entities.User;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Mapper interface for converting between User entities and DTOs.
 * Uses MapStruct for high-performance compile-time code generation.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    /**
     * Maps Registration DTO to User Entity.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    User toEntity(CreateUserRequestDTO dto);

    /**
     * Maps User Entity to GeneralResponse DTO for API output.
     */
    UserResponseDTO toResponseDTO(User user);

    /**
     * Updates an existing User entity with data from UpdateUserRequestDTO.
     * The @MappingTarget annotation ensures the existing instance is modified.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true) // Security reason
    @Mapping(target = "lastUpdateDate", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateUserFromDto(UpdateUserRequestDTO dto, @MappingTarget User user);

    /**
     * Specifically updates an existing Address entity from an AddressDTO.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateAddressFromDto(AddressDTO dto, @MappingTarget Address address);

    /**
     * Este método é executado automaticamente pelo MapStruct
     * logo após o mapeamento de updateUserFromDto ser concluído.
     */
    @AfterMapping
    private void updateLastUpdateDate(@MappingTarget User user) {
        user.setLastUpdateDate(LocalDateTime.now());
    }
}