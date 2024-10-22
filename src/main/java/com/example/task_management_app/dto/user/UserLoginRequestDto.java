package com.example.task_management_app.dto.user;

public record UserLoginRequestDto(
        String email,
        String password
) {
}
