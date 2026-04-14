package br.com.user.service.auth.domain;

public record GeneralResponse(
        int code,
        String message,
        boolean isValid
) {
}
