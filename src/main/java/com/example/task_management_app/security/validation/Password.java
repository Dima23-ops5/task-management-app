package com.example.task_management_app.security.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordValidator.class)
@Target(value = {ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {
    String message() default "Invalid format password. "
            + "Password need to contains at least one small character, one capital character,"
            + "and the maximum length of 20 characters.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
