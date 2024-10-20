package com.example.task_management_app.controller;

import com.example.task_management_app.dto.UserDto;
import com.example.task_management_app.dto.UserLoginRequestDto;
import com.example.task_management_app.dto.UserLoginResponseDto;
import com.example.task_management_app.dto.UserRegistrationRequestDto;
import com.example.task_management_app.repository.UserRepository;
import com.example.task_management_app.security.AuthenticationService;
import com.example.task_management_app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto register(@RequestBody @Valid UserRegistrationRequestDto requestDto) {
        return userService.registerUser(requestDto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }
}
