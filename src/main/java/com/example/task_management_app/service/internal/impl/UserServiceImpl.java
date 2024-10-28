package com.example.task_management_app.service.internal.impl;

import com.example.task_management_app.dto.user.UserDto;
import com.example.task_management_app.dto.user.UserRegistrationRequestDto;
import com.example.task_management_app.dto.user.UserUpdateRequestDto;
import com.example.task_management_app.dto.user.UserUpdateRoleDto;
import com.example.task_management_app.exception.RegistrationException;
import com.example.task_management_app.mapper.UserMapper;
import com.example.task_management_app.model.Role;
import com.example.task_management_app.model.User;
import com.example.task_management_app.repository.RoleRepository;
import com.example.task_management_app.repository.UserRepository;
import com.example.task_management_app.service.internal.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.HashSet;
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
    @Transactional
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

    @Override
    @Transactional
    public UserDto updateUserRoleById(Long userId, UserUpdateRoleDto updateRoleDto) {
        User user = userRepository.findUserById(userId).orElseThrow(
                () -> new EntityNotFoundException("Cannot find user with id: " + userId)
        );
        Role.RoleName role = Role.RoleName.valueOf(updateRoleDto.roleName().trim().toUpperCase());
        Set<Role> roles = new HashSet<>();
        roles.add((roleRepository.findRoleByRoleName(role)
                .orElseThrow(
                        () -> new EntityNotFoundException("Cannot find role with role name: "
                                + updateRoleDto.roleName()))));
        user.setRole(roles);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.findUserById(userId).orElseThrow(
                () -> new EntityNotFoundException("Cannot find user with id: " + userId)
        );

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(Long userId, UserUpdateRequestDto userUpdateRequestDto) {
        User user = userRepository.findUserById(userId).orElseThrow(
                () -> new EntityNotFoundException("Cannot find user with id: " + userId)
        );
        User afterUpdate = userMapper.updateUser(user, userUpdateRequestDto);
        afterUpdate.setRole(user.getRole());
        afterUpdate.setPassword(user.getPassword());
        return userMapper.toDto(userRepository.save(afterUpdate));
    }
}
