package com.example.task_management_app.service;

import com.example.task_management_app.dto.UserDto;
import com.example.task_management_app.dto.UserRegistrationRequestDto;

public interface UserService {
    UserDto registerUser(UserRegistrationRequestDto requestDto);
}
