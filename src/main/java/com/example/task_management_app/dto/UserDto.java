package com.example.task_management_app.dto;

public record UserDto(
        Long id,
        String userName,
        String firstName,
        String lastName,
        String email
) {
}
