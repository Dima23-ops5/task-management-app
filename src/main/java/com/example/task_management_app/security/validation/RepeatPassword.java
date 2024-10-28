package com.example.task_management_app.security.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Constraint(validatedBy = RepeatPasswordValidator.class)
@Target(value = {ElementType.METHOD, ElementType.FIELD})
public @interface RepeatPassword {
    String message() default "Repeat password must be same as password";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
