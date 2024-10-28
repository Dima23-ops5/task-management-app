package com.example.task_management_app.dto.user;

import com.example.task_management_app.security.validation.Email;
import com.example.task_management_app.security.validation.Password;
import com.example.task_management_app.security.validation.RepeatPassword;
import jakarta.validation.constraints.NotBlank;

public record UserRegistrationRequestDto(
        @NotBlank(message = "User name can't be null") String userName,
        @NotBlank(message = "First name can't be null") String firstName,
        @NotBlank(message = "Last name can't be null") String lastName,
        @Email String email,
        @Password String password,
        @RepeatPassword String repeatPassword

) {
}
