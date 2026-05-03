package br.com.user.service.auth.service;

import br.com.user.service.auth.dto.*;
import br.com.user.service.auth.entities.Address;
import br.com.user.service.auth.entities.User;
import br.com.user.service.auth.exceptions.BusinessException;
import br.com.user.service.auth.exceptions.InvalidCredentialsException;
import br.com.user.service.auth.exceptions.UserNotFoundException;
import br.com.user.service.auth.mapper.UserMapper;
import br.com.user.service.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private CreateUserRequestDTO createUserRequestDTO;
    private UserResponseDTO userResponseDTO;
    private UpdateUserRequestDTO updateUserRequestDTO;
    private UpdatePasswordRequestDTO updatePasswordRequestDTO;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        Address address = Address.builder()
                .id(1L)
                .street("Rua Teste")
                .number("123")
                .city("Cidade Teste")
                .zipCode("12345-678")
                .deleted(false)
                .build();

        user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .login("testuser")
                .password("encodedPassword")
                .type("CLIENT")
                .address(address)
                .lastUpdateDate(LocalDateTime.now())
                .deleted(false)
                .build();

        createUserRequestDTO = CreateUserRequestDTO.builder()
                .name("New User")
                .email("new@example.com")
                .login("newuser")
                .password("rawPassword")
                .type("CLIENT")
                .build();

        userResponseDTO = UserResponseDTO.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .login("testuser")
                .type("CLIENT")
                .build();

        updateUserRequestDTO = UpdateUserRequestDTO.builder()
                .name("Updated Name")
                .email("updated@example.com")
                .type("CLIENT")
                .build();

        updatePasswordRequestDTO = new UpdatePasswordRequestDTO(
                "rawPassword", "newRawPassword"
        );

        pageable = PageRequest.of(0, 10);
    }

    @Test
    @DisplayName("Should create a user successfully")
    void shouldCreateUserSuccessfully() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userMapper.toEntity(any(CreateUserRequestDTO.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponseDTO(any(User.class))).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.create(createUserRequestDTO);

        assertNotNull(result);
        assertEquals(userResponseDTO.getEmail(), result.getEmail());
        verify(userRepository, times(1)).findByEmail(createUserRequestDTO.getEmail());
        verify(userMapper, times(1)).toEntity(createUserRequestDTO);
        verify(passwordEncoder, times(1)).encode(createUserRequestDTO.getPassword());
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toResponseDTO(user);
    }

    @Test
    @DisplayName("Should throw BusinessException when email already exists during creation")
    void shouldThrowBusinessExceptionWhenEmailExists() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        BusinessException exception = assertThrows(BusinessException.class, () ->
                userService.create(createUserRequestDTO));

        assertEquals("The email address is already registered in our system.", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(createUserRequestDTO.getEmail());
    }

    @Test
    @DisplayName("Should find users by name successfully with pagination")
    void shouldFindUsersByNameSuccessfullyWithPagination() {
        Page<User> userPage = new PageImpl<>(List.of(user), pageable, 1);
        when(userRepository.findByNameContainingIgnoreCase(anyString(), any(Pageable.class))).thenReturn(userPage);
        when(userMapper.toResponseDTO(any(User.class))).thenReturn(userResponseDTO);

        Page<UserResponseDTO> result = userService.findByName("Test", pageable);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(userResponseDTO.getName(), result.getContent().getFirst().getName());
        verify(userRepository, times(1)).findByNameContainingIgnoreCase(eq("Test"), eq(pageable));
    }

    @Test
    @DisplayName("Should find all users when name is null or empty with pagination")
    void shouldFindAllUsersWhenNameIsNullOrEmptyWithPagination() {
        Page<User> userPage = new PageImpl<>(List.of(user), pageable, 1);
        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);
        when(userMapper.toResponseDTO(any(User.class))).thenReturn(userResponseDTO);

        Page<UserResponseDTO> result = userService.findByName(null, pageable);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(userResponseDTO.getName(), result.getContent().getFirst().getName());
        verify(userRepository, times(1)).findAll(eq(pageable));
        verify(userRepository, never()).findByNameContainingIgnoreCase(anyString(), any(Pageable.class));

        result = userService.findByName("", pageable);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(userResponseDTO.getName(), result.getContent().getFirst().getName());
        verify(userRepository, times(2)).findAll(eq(pageable)); // Called twice
        verify(userRepository, never()).findByNameContainingIgnoreCase(anyString(), any(Pageable.class));
    }

    @Test
    @DisplayName("Should update user successfully without address change")
    void shouldUpdateUserSuccessfullyWithoutAddressChange() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        doNothing().when(userMapper).updateUserFromDto(any(UpdateUserRequestDTO.class), any(User.class));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponseDTO(any(User.class))).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.update(1L, updateUserRequestDTO);

        assertNotNull(result);
        verify(userMapper, never()).updateAddressFromDto(any(), any());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should update user successfully with address change")
    void shouldUpdateUserSuccessfullyWithAddressChange() {
        AddressDTO addressDTO = new AddressDTO("New St", "123", "City", "12345");
        updateUserRequestDTO.setAddress(addressDTO);
        
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        doNothing().when(userMapper).updateUserFromDto(any(UpdateUserRequestDTO.class), any(User.class));
        doNothing().when(userMapper).updateAddressFromDto(any(AddressDTO.class), any(Address.class));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponseDTO(any(User.class))).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.update(1L, updateUserRequestDTO);

        assertNotNull(result);
        verify(userMapper, times(1)).updateAddressFromDto(any(AddressDTO.class), any(Address.class));
    }

    @Test
    @DisplayName("Should delete user successfully (soft delete)")
    void shouldDeleteUserSuccessfully() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.delete(1L);

        assertTrue(user.isDeleted());
        assertTrue(user.getAddress().isDeleted());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should find all users successfully with pagination")
    void shouldFindAllUsersSuccessfullyWithPagination() {
        Page<User> userPage = new PageImpl<>(List.of(user), pageable, 1);
        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);
        when(userMapper.toResponseDTO(any(User.class))).thenReturn(userResponseDTO);

        Page<UserResponseDTO> result = userService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(userResponseDTO.getName(), result.getContent().getFirst().getName());
        verify(userRepository, times(1)).findAll(eq(pageable));
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when user not found during password update")
    void shouldThrowUserNotFoundExceptionWhenUserNotFoundDuringPasswordUpdate() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                userService.updatePassword(1L, updatePasswordRequestDTO));
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when current password does not match")
    void shouldThrowInvalidCredentialsExceptionWhenPasswordDoesNotMatch() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () ->
                userService.updatePassword(1L, updatePasswordRequestDTO));
    }

    @Test
    @DisplayName("Should update password successfully")
    void shouldUpdatePasswordSuccessfully() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.updatePassword(1L, updatePasswordRequestDTO);

        assertEquals("newEncodedPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }
}
