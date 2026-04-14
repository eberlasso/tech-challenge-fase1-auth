package br.com.user.service.auth.dto.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserTypeValidator.class)
public @interface ValidUserType {
    String message() default "Invalid user type code. Use 'RO' to RESTAURANT_OWNER or 'CL' to CLIENT";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
