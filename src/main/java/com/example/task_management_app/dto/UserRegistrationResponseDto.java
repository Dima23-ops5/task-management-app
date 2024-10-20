package com.example.task_management_app.dto;

public record UserRegistrationResponseDto(
        String userName,
        String firstName,
        String lastName,
        String email
) {
}
