package com.example.task_management_app.service.impl;

import com.example.task_management_app.dto.UserDto;
import com.example.task_management_app.dto.UserRegistrationRequestDto;
import com.example.task_management_app.exception.RegistrationException;
import com.example.task_management_app.mapper.UserMapper;
import com.example.task_management_app.model.Role;
import com.example.task_management_app.model.User;
import com.example.task_management_app.repository.RoleRepository;
import com.example.task_management_app.repository.UserRepository;
import com.example.task_management_app.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserDto registerUser(UserRegistrationRequestDto requestDto) {
        if (userRepository.findUserByEmail(requestDto.email()).isPresent()) {
            throw new RegistrationException("User already exists");
        }
        User user = userMapper.toEntity(requestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Set.of(roleRepository.findRoleByRoleName(Role.RoleName.USER)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Cannot find role with name:" + Role.RoleName.USER
                        )
                )));
        return userMapper.toDto(userRepository.save(user));
    }
}
