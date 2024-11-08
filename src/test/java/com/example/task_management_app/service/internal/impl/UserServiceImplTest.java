package com.example.task_management_app.service.internal.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.example.task_management_app.dto.user.UserDto;
import com.example.task_management_app.dto.user.UserRegistrationRequestDto;
import com.example.task_management_app.dto.user.UserUpdateRequestDto;
import com.example.task_management_app.dto.user.UserUpdateRoleDto;
import com.example.task_management_app.mapper.UserMapper;
import com.example.task_management_app.model.Role;
import com.example.task_management_app.model.User;
import com.example.task_management_app.repository.RoleRepository;
import com.example.task_management_app.repository.UserRepository;
import java.util.Optional;
import java.util.Set;
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
        User user = createBobUser();

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

    @Test
    @DisplayName(value = "Should return user with updated role")
    public void updateUserRoleByUserId_Ok() {
        User user = createBobUser();
        Role role = new Role();
        role.setId(1L);
        role.setRoleName(Role.RoleName.USER);
        user.setRole(Set.of(role));

        Role expectedRole = new Role();
        expectedRole.setId(2L);
        expectedRole.setRoleName(Role.RoleName.ADMIN);

        User expected = new User();
        expected.setId(user.getId());
        expected.setUserName(user.getUserName());
        expected.setFirstName(user.getFirstName());
        expected.setLastName(user.getLastName());
        expected.setEmail(user.getEmail());
        expected.setPassword(user.getPassword());
        expected.setRole(Set.of(expectedRole));

        UserDto expectedUserDto = userToUserDto(expected);

        UserUpdateRoleDto updateRoleDto = new UserUpdateRoleDto("ADMIN");

        when(userRepository.findUserById(anyLong())).thenReturn(Optional.of(user));
        when(roleRepository.findRoleByRoleName(any())).thenReturn(Optional.of(expectedRole));
        when(userRepository.save(user)).thenReturn(expected);
        when(userMapper.toDto(expected)).thenReturn(expectedUserDto);

        UserDto actual = userServiceImpl.updateUserRoleById(user.getId(), updateRoleDto);

        assertNotNull(actual);
        assertEquals(expectedUserDto, actual);

    }

    @Test
    @DisplayName(value = "Should find and return user by they id")
    public void findUserById_Ok() {
        User user = createBobUser();

        UserDto expectedUserDto = userToUserDto(user);

        when(userRepository.findUserById(anyLong())).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(expectedUserDto);

        UserDto actual = userServiceImpl.getUserById(user.getId());

        assertNotNull(actual);
        assertEquals(expectedUserDto, actual);
    }

    @Test
    @DisplayName(value = "Should update and return updated user")
    public void updateUserById_Ok() {
        User user = createBobUser();

        UserUpdateRequestDto requestDto =
                new UserUpdateRequestDto("Alice",
                        "Alice", "Bennett",
                        "alice@gmail.com");
        User expected = new User();
        expected.setId(user.getId());
        expected.setUserName(requestDto.userName());
        expected.setFirstName(requestDto.firstName());
        expected.setLastName(requestDto.lastName());
        expected.setEmail(requestDto.email());
        expected.setRole(user.getRole());
        expected.setPassword(user.getPassword());

        UserDto expectedUserDto = userToUserDto(expected);

        when(userRepository.findUserById(anyLong())).thenReturn(Optional.of(user));
        when(userMapper.updateUser(user, requestDto)).thenReturn(expected);
        when(userRepository.save(expected)).thenReturn(expected);
        when(userMapper.toDto(expected)).thenReturn(expectedUserDto);

        UserDto actual = userServiceImpl.updateUser(user.getId(), requestDto);

        assertNotNull(actual);
        assertEquals(expectedUserDto, actual);
    }

    private User createBobUser() {
        User user = new User();
        user.setId(1L);
        user.setUserName("bob123");
        user.setPassword("Bobpassword");
        user.setEmail("bob@gmail.com");
        user.setFirstName("Bob");
        user.setLastName("Petrenko");
        return user;
    }

    private UserDto userToUserDto(User user) {
        UserDto userDto = new UserDto(user.getId(),
                user.getUserName(), user.getFirstName(),
                user.getLastName(), user.getEmail());
        return userDto;
    }
}
