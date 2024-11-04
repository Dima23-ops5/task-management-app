package com.example.task_management_app.service.internal.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.example.task_management_app.dto.user.UserDto;
import com.example.task_management_app.dto.user.UserRegistrationRequestDto;
import com.example.task_management_app.mapper.UserMapper;
import com.example.task_management_app.model.Role;
import com.example.task_management_app.model.User;
import com.example.task_management_app.repository.RoleRepository;
import com.example.task_management_app.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    @DisplayName(value = "Register users correct")
    public void registerUser_Ok() {
        User user = new User();
        user.setId(1L);
        user.setUserName("bob123");
        user.setPassword("Bobpassword");
        user.setEmail("bob@gmail.com");
        user.setFirstName("Bob");
        user.setLastName("Petrenko");

        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto(
                user.getUsername(),
                user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getPassword(), user.getPassword());

        Role userRole = new Role();
        userRole.setId(1L);
        userRole.setRoleName(Role.RoleName.USER);

        UserDto excepted = new UserDto(user.getId(), user.getUsername(), user.getFirstName(),
                user.getLastName(), user.getEmail());
        when(userMapper.toEntity(requestDto)).thenReturn(user);
        when(passwordEncoder.encode(user.getPassword())).thenReturn(user.getPassword());
        when(roleRepository.findRoleByRoleName(userRole.getRoleName()))
                .thenReturn(Optional.of(userRole));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(excepted);
        UserDto actual = userServiceImpl.registerUser(requestDto);

        assertNotNull(actual);
        assertEquals(excepted, actual);
    }

}
