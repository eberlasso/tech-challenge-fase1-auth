package br.com.user.service.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para requisição de troca de senha.
 * Centraliza as informações necessárias para validar a identidade do usuário
 * antes de aplicar a nova credencial.
 */
public record UpdatePasswordRequestDTO(
        /**
         * Senha atual do usuário para fins de validação.
         */
        @NotBlank(message = "A senha atual é obrigatória")
        String currentPassword,

        /**
         * Nova senha a ser definida no sistema.
         */
        @NotBlank(message = "A nova senha é obrigatória")
        String newPassword
) {}
