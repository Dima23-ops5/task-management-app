package com.example.task_management_app.dto;

public record UserRegistrationRequestDto(
        String userName,
        String firstName,
        String lastName,
        String email,
        String password,
        String repeatPassword

) {
}
