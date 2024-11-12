package com.example.task_management_app.controller;

import com.example.task_management_app.dto.user.UserDto;
import com.example.task_management_app.dto.user.UserLoginRequestDto;
import com.example.task_management_app.dto.user.UserLoginResponseDto;
import com.example.task_management_app.dto.user.UserRegistrationRequestDto;
import com.example.task_management_app.security.AuthenticationService;
import com.example.task_management_app.service.internal.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication management",
        description = "Endpoints for managing authentication of users")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registration users", description = "Create and register a new user")
    public UserDto register(@RequestBody @Valid UserRegistrationRequestDto requestDto) {
        return userService.registerUser(requestDto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Login user",
            description = "Log in the user if they already exist and return the user's token")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }
}
