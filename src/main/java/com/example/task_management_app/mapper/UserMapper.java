package com.example.task_management_app.mapper;

import com.example.task_management_app.config.MapperConfig;
import com.example.task_management_app.dto.UserDto;
import com.example.task_management_app.dto.UserRegistrationRequestDto;
import com.example.task_management_app.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    @Mapping(target = "userName", source = "userName")
    UserDto toDto(User user);

    User toEntity(UserRegistrationRequestDto requestDto);
}
