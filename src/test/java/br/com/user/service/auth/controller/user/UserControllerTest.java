package br.com.user.service.auth.controller.user;

import br.com.user.service.auth.dto.CreateUserRequestDTO;
import br.com.user.service.auth.dto.UpdatePasswordRequestDTO;
import br.com.user.service.auth.dto.UpdateUserRequestDTO;
import br.com.user.service.auth.dto.UserResponseDTO;
import br.com.user.service.auth.entities.Address;
import br.com.user.service.auth.entities.User;
import br.com.user.service.auth.exceptions.GlobalExceptionHandler;
import br.com.user.service.auth.mapper.UserMapper;
import br.com.user.service.auth.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private UserMapper userMapper;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setValidator(new org.springframework.validation.beanvalidation.LocalValidatorFactoryBean())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Should create a user successfully")
    void createUser_Success() throws Exception {
        CreateUserRequestDTO requestDTO = CreateUserRequestDTO.builder()
                .name("Test User")
                .email("test@example.com")
                .login("testlogin")
                .password("password123")
                .type("CL")
                .build();

        when(userService.create(any(CreateUserRequestDTO.class))).thenReturn(new UserResponseDTO());

        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.message").value("USER CREATED SUCCESSFULLY"))
                .andExpect(jsonPath("$.isValid").value(true));

        verify(userService, times(1)).create(any(CreateUserRequestDTO.class));
    }

    @Test
    @DisplayName("Should return bad request when creating user with invalid email")
    void createUser_InvalidEmail() throws Exception {
        CreateUserRequestDTO requestDTO = CreateUserRequestDTO.builder()
                .name("Test User")
                .email("invalid-email")
                .login("testlogin")
                .password("password123")
                .type("CL")
                .build();

        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).create(any(CreateUserRequestDTO.class));
    }

    @Test
    @DisplayName("Should update a user successfully")
    void updateUser_Success() throws Exception {
        Long userId = 1L;
        UpdateUserRequestDTO requestDTO = UpdateUserRequestDTO.builder()
                .name("Updated Name")
                .email("updated@example.com")
                .type("CL")
                .build();

        when(userService.update(anyLong(), any(UpdateUserRequestDTO.class))).thenReturn(new UserResponseDTO());

        mockMvc.perform(put("/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(2))
                .andExpect(jsonPath("$.message").value("USER UPDATED SUCCESSFULLY"))
                .andExpect(jsonPath("$.isValid").value(true));

        verify(userService, times(1)).update(eq(userId), any(UpdateUserRequestDTO.class));
    }

    @Test
    @DisplayName("Should delete a user successfully")
    void deleteUser_Success() throws Exception {
        Long userId = 1L;

        doNothing().when(userService).delete(anyLong());

        mockMvc.perform(delete("/v1/users/{id}", userId))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).delete(eq(userId));
    }

    @Test
    @DisplayName("Should search users by name successfully")
    void searchByName_Success() throws Exception {
        String name = "Test";
        UserResponseDTO user1 = UserResponseDTO.builder().id(1L).name("Test User 1").email("test1@example.com").build();
        List<UserResponseDTO> userList = List.of(user1);

        when(userService.findByName(name)).thenReturn(userList);

        mockMvc.perform(get("/v1/users/search")
                        .param("name", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(user1.getId()))
                .andExpect(jsonPath("$[0].name").value(user1.getName()));

        verify(userService, times(1)).findByName(eq(name));
    }

    @Test
    @DisplayName("Should update password successfully")
    void updatePassword_Success() throws Exception {
        Long userId = 1L;
        UpdatePasswordRequestDTO requestDTO = new UpdatePasswordRequestDTO("oldPassword", "newPassword123");

        doNothing().when(userService).updatePassword(anyLong(), any(UpdatePasswordRequestDTO.class));

        mockMvc.perform(patch("/v1/users/{id}/password", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).updatePassword(eq(userId), any(UpdatePasswordRequestDTO.class));
    }

    @Test
    @DisplayName("Should return internal server error when service throws exception")
    void service_ThrowsException() throws Exception {
        Long userId = 1L;
        doThrow(new RuntimeException("Unexpected error")).when(userService).delete(anyLong());

        mockMvc.perform(delete("/v1/users/{id}", userId))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.title").value("Internal Server Error"));
    }
}
