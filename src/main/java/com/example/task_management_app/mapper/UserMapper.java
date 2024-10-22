package com.example.task_management_app.mapper;

import com.example.task_management_app.config.MapperConfig;
import com.example.task_management_app.dto.user.UserDto;
import com.example.task_management_app.dto.user.UserRegistrationRequestDto;
import com.example.task_management_app.dto.user.UserUpdateRequestDto;
import com.example.task_management_app.model.User;
import java.util.HashSet;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    @Mapping(target = "userName", source = "userName")
    UserDto toDto(User user);

    User toEntity(UserRegistrationRequestDto requestDto);

    User updateUser(@MappingTarget User user, UserUpdateRequestDto requestDto);

    @Named("usersToUserDtos")
    default Set<UserDto> usersToUserDtos(Set<User> userList) {
        Set<UserDto> userDtos = new HashSet<>();
        for (User user : userList) {
            userDtos.add(toDto(user));
        }
        return userDtos;
    }
}
