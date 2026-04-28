package br.com.user.service.auth.dto.validator;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class UserTypeValidatorTest {

    private UserTypeValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new UserTypeValidator();
        context = mock(ConstraintValidatorContext.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"CL", "RO"})
    @DisplayName("Should return true for valid user types")
    void shouldReturnTrueForValidUserTypes(String type) {
        assertTrue(validator.isValid(type, context));
    }

    @ParameterizedTest
    @ValueSource(strings = {"CLIENT", "OWNER", "ADMIN", "", " "})
    @DisplayName("Should return false for invalid user types")
    void shouldReturnFalseForInvalidUserTypes(String type) {
        assertFalse(validator.isValid(type, context));
    }

    @Test
    @DisplayName("Should return false for null user type")
    void shouldReturnFalseForNullUserType() {
        assertFalse(validator.isValid(null, context));
    }
}
