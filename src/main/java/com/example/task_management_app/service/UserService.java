package com.example.task_management_app.service;

import com.example.task_management_app.dto.UserDto;
import com.example.task_management_app.dto.UserRegistrationRequestDto;
import com.example.task_management_app.dto.UserUpdateRequestDto;
import com.example.task_management_app.dto.UserUpdateRoleDto;

public interface UserService {
    UserDto registerUser(UserRegistrationRequestDto requestDto);

    UserDto updateUserRoleById(Long userId, UserUpdateRoleDto userUpdateRoleDto);

    UserDto getUserById(Long userId);

    UserDto updateUser(Long userId, UserUpdateRequestDto userUpdateRequestDto);
}
