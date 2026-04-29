package br.com.user.service.auth.mapper;

import br.com.user.service.auth.dto.AddressDTO;
import br.com.user.service.auth.dto.CreateUserRequestDTO;
import br.com.user.service.auth.dto.UpdateUserRequestDTO;
import br.com.user.service.auth.dto.UserResponseDTO;
import br.com.user.service.auth.entities.Address;
import br.com.user.service.auth.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
    @DisplayName("Should map CreateUserRequestDTO to User entity")
    void shouldMapCreateUserRequestDtoToUserEntity() {
        AddressDTO addressDTO = new AddressDTO("Rua A", "10", "Cidade X", "12345");
        CreateUserRequestDTO dto = CreateUserRequestDTO.builder()
                .name("Test User")
                .email("test@example.com")
                .login("testlogin")
                .password("rawpassword")
                .type("CLIENT")
                .address(addressDTO)
                .build();

        User user = userMapper.toEntity(dto);

        assertNotNull(user);
        assertNull(user.getId()); // Ignored
        assertEquals(dto.getName(), user.getName());
        assertEquals(dto.getEmail(), user.getEmail());
        assertEquals(dto.getLogin(), user.getLogin());
        assertNull(user.getPassword()); // Ignored
        assertEquals(dto.getType(), user.getType());
        assertNotNull(user.getAddress());
        assertEquals(addressDTO.getStreet(), user.getAddress().getStreet());
        assertNull(user.getLastUpdateDate()); // Ignored in toEntity, set by @AfterMapping on update
        assertFalse(user.isDeleted()); // Default value
    }

    @Test
    @DisplayName("Should map User entity to UserResponseDTO")
    void shouldMapUserEntityToUserResponseDto() {
        Address address = Address.builder()
                .id(1L)
                .street("Rua B")
                .number("20")
                .city("Cidade Y")
                .zipCode("67890")
                .deleted(false)
                .build();
        User user = User.builder()
                .id(1L)
                .name("Response User")
                .email("response@example.com")
                .login("resplogin")
                .password("encoded")
                .type("RO")
                .address(address)
                .lastUpdateDate(LocalDateTime.now())
                .deleted(false)
                .build();

        UserResponseDTO dto = userMapper.toResponseDTO(user);

        assertNotNull(dto);
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getName(), dto.getName());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getLogin(), dto.getLogin());
        assertEquals(user.getType(), dto.getType());
        assertNotNull(dto.getAddress());
        assertEquals(user.getAddress().getStreet(), dto.getAddress().getStreet());
        assertEquals(user.getLastUpdateDate(), dto.getLastUpdateDate());
    }

    @Test
    @DisplayName("Should update existing User entity from UpdateUserRequestDTO")
    void shouldUpdateExistingUserEntityFromUpdateUserRequestDto() {
        Address existingAddress = Address.builder()
                .id(1L)
                .street("Old Street")
                .number("1")
                .city("Old City")
                .zipCode("11111")
                .deleted(false)
                .build();
        User existingUser = User.builder()
                .id(1L)
                .name("Original Name")
                .email("original@example.com")
                .login("original")
                .password("originalpass")
                .type("CLIENT")
                .address(existingAddress)
                .lastUpdateDate(LocalDateTime.of(2023, 1, 1, 10, 0))
                .deleted(false)
                .build();

        AddressDTO updateAddressDTO = new AddressDTO("New Street", "2", "New City", "22222");
        UpdateUserRequestDTO updateDto = UpdateUserRequestDTO.builder()
                .name("Updated Name")
                .email("updated@example.com")
                .type("RO")
                .address(updateAddressDTO)
                .build();

        userMapper.updateUserFromDto(updateDto, existingUser);

        assertEquals("Updated Name", existingUser.getName());
        assertEquals("updated@example.com", existingUser.getEmail());
        assertEquals("RO", existingUser.getType());
        // Password and ID should not change
        assertEquals("originalpass", existingUser.getPassword());
        assertEquals(1L, existingUser.getId());
        // Address should be updated by updateAddressFromDto, which is called separately in service
        // Here we only check if the address object itself is not null
        assertNotNull(existingUser.getAddress());
        // lastUpdateDate should be updated by @AfterMapping
        assertNotNull(existingUser.getLastUpdateDate());
        assertTrue(existingUser.getLastUpdateDate().isEqual(LocalDateTime.of(2023, 1, 1, 10, 0)));
    }

    @Test
    @DisplayName("Should update existing Address entity from AddressDTO")
    void shouldUpdateExistingAddressEntityFromAddressDto() {
        Address existingAddress = Address.builder()
                .id(1L)
                .street("Old Street")
                .number("1")
                .city("Old City")
                .zipCode("11111")
                .deleted(false)
                .build();
        AddressDTO updateDto = new AddressDTO("New Street", "2", "New City", "22222");

        userMapper.updateAddressFromDto(updateDto, existingAddress);

        assertEquals("New Street", existingAddress.getStreet());
        assertEquals("2", existingAddress.getNumber());
        assertEquals("New City", existingAddress.getCity());
        assertEquals("22222", existingAddress.getZipCode());
        assertEquals(1L, existingAddress.getId()); // ID should not change
        assertFalse(existingAddress.isDeleted()); // Deleted should not change
    }
}
