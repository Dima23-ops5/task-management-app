package com.example.task_management_app.dto;

public record UserLoginRequestDto(
        String email,
        String password
) {
}
