package com.example.task_management_app.dto.user;

import com.example.task_management_app.security.validation.Email;
import com.example.task_management_app.security.validation.Password;

public record UserLoginRequestDto(
        @Email String email,
        @Password String password
) {
}
