package com.example.task_management_app.security.validation;

import com.example.task_management_app.dto.user.UserRegistrationRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RepeatPasswordValidator implements ConstraintValidator<RepeatPassword,
        UserRegistrationRequestDto> {
    @Override
    public boolean isValid(UserRegistrationRequestDto userRegistrationRequestDto,
                           ConstraintValidatorContext constraintValidatorContext) {
        return userRegistrationRequestDto.password().equals(
                userRegistrationRequestDto.repeatPassword());
    }
}
