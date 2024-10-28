package com.example.task_management_app.controller;

import com.example.task_management_app.dto.user.UserDto;
import com.example.task_management_app.dto.user.UserUpdateRequestDto;
import com.example.task_management_app.dto.user.UserUpdateRoleDto;
import com.example.task_management_app.model.User;
import com.example.task_management_app.service.internal.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User management", description = "Endpoints for managing users")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("/{id}/role")
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @Operation(summary = "Update user's role",
            description = "Updating user's role by user id and role name,"
                    + " update can only administrator")
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateUserRole(@PathVariable(value = "id") @Positive Long userId,
                                  @RequestBody @Valid UserUpdateRoleDto userUpdateRoleDto) {
        return userService.updateUserRoleById(userId, userUpdateRoleDto);
    }

    @GetMapping("/me")
    @PreAuthorize(value = "hasAuthority('USER')")
    @Operation(summary = "Get information about the current user",
            description = "Retrieve and return information about the currently authenticated user.")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getAllInformationAboutUser(Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        return userService.getUserById(userId);
    }

    @PatchMapping("/me")
    @PreAuthorize(value = "hasAuthority('USER')")
    @Operation(summary = "Update user information",
            description = "Update the authenticated user's personal profile information.")
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateUser(Authentication authentication,
                              @RequestBody @Valid UserUpdateRequestDto userUpdateRequestDto) {
        Long userId = getCurrentUserId(authentication);
        return userService.updateUser(userId, userUpdateRequestDto);
    }

    private Long getCurrentUserId(Authentication authentication) {
        User user = (User)authentication.getPrincipal();
        return user.getId();
    }
}
