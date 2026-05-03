package br.com.user.service.auth.dto;

import br.com.user.service.auth.domain.GeneralResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DtoAndDomainTest {

    @Test
    @DisplayName("Test GeneralResponse")
    void testGeneralResponse() {
        GeneralResponse response = new GeneralResponse(200, "Success", true);
        assertEquals(200, response.code());
        assertEquals("Success", response.message());
        assertTrue(response.isValid());
        
        GeneralResponse response2 = new GeneralResponse(400, "Error", false);
        assertEquals(400, response2.code());
        assertEquals("Error", response2.message());
        assertFalse(response2.isValid());
    }

    @Test
    @DisplayName("Test UserResponseDTO")
    void testUserResponseDTO() {
        LocalDateTime now = LocalDateTime.now();
        AddressDTO address = new AddressDTO("Rua", "123", "Cidade", "12345");
        
        UserResponseDTO dto = UserResponseDTO.builder()
                .id(1L)
                .name("Name")
                .email("email@test.com")
                .login("login")
                .type("CL")
                .address(address)
                .lastUpdateDate(now)
                .build();

        assertEquals(1L, dto.getId());
        assertEquals("Name", dto.getName());
        assertEquals("email@test.com", dto.getEmail());
        assertEquals("login", dto.getLogin());
        assertEquals("CL", dto.getType());
        assertEquals(address, dto.getAddress());
        assertEquals(now, dto.getLastUpdateDate());

        UserResponseDTO emptyDto = new UserResponseDTO();
        assertNotNull(emptyDto);
    }

    @Test
    @DisplayName("Test AddressDTO")
    void testAddressDTO() {
        AddressDTO dto = new AddressDTO("Rua", "123", "Cidade", "12345");
        assertEquals("Rua", dto.getStreet());
        assertEquals("123", dto.getNumber());
        assertEquals("Cidade", dto.getCity());
        assertEquals("12345", dto.getZipCode());

        AddressDTO empty = new AddressDTO();
        empty.setStreet("S");
        assertEquals("S", empty.getStreet());
    }

    @Test
    @DisplayName("Test CreateUserRequestDTO")
    void testCreateUserRequestDTO() {
        AddressDTO address = new AddressDTO("Rua", "123", "Cidade", "12345");
        CreateUserRequestDTO dto = CreateUserRequestDTO.builder()
                .name("Name")
                .email("a@a.com")
                .login("l")
                .password("p")
                .type("CL")
                .address(address)
                .build();

        assertEquals("Name", dto.getName());
        assertEquals("p", dto.getPassword());
        assertEquals(address, dto.getAddress());
        
        CreateUserRequestDTO empty = new CreateUserRequestDTO();
        assertNotNull(empty);
    }

    @Test
    @DisplayName("Test UpdateUserRequestDTO")
    void testUpdateUserRequestDTO() {
        UpdateUserRequestDTO dto = UpdateUserRequestDTO.builder()
                .name("N")
                .email("e")
                .type("RO")
                .build();
        assertEquals("N", dto.getName());
        
        UpdateUserRequestDTO empty = new UpdateUserRequestDTO();
        assertNotNull(empty);
    }

    @Test
    @DisplayName("Test LoginRequestDTO (Record)")
    void testLoginRequestDTO() {
        LoginRequestDTO dto = new LoginRequestDTO("user", "pass", "email@email.com.br");
        assertEquals("user", dto.login());
        assertEquals("pass", dto.password());
    }

    @Test
    @DisplayName("Test LoginResponseDTO (Record)")
    void testLoginResponseDTO() {
        LoginResponseDTO dto = new LoginResponseDTO("token");
        assertEquals("token", dto.token());
    }

    @Test
    @DisplayName("Test UpdatePasswordRequestDTO (Record)")
    void testUpdatePasswordRequestDTO() {
        UpdatePasswordRequestDTO dto = new UpdatePasswordRequestDTO("old", "new");
        assertEquals("old", dto.currentPassword());
        assertEquals("new", dto.newPassword());
    }
}
