package com.example.task_management_app.dto;

public record UserUpdateRequestDto(
        String userName,
        String firstName,
        String lastName,
        String email
) {
}
