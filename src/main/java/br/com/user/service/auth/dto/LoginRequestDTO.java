package br.com.user.service.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "Login cannot be empty")
        String login,
        @NotBlank(message = "Password cannot be empty")
        String password,
        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Invalid email format")
        String email
) {}