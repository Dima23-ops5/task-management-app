package com.example.task_management_app.dto.user;

public record UserUpdateRequestDto(
        String userName,
        String firstName,
        String lastName,
        String email
) {
}
