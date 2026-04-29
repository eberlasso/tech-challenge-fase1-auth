package br.com.user.service.auth.dto.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class UserTypeValidator implements ConstraintValidator<ValidUserType, String> {

    private final List<String> allowedCodes = Arrays.asList("CL", "RO");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false; // @NotBlank já cuida disso se você usar ambos
        }
        return allowedCodes.contains(value.toUpperCase());
    }
}