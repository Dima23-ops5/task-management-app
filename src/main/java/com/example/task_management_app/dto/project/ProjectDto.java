package com.example.task_management_app.dto.project;

import com.example.task_management_app.dto.user.UserDto;
import java.time.LocalDate;
import java.util.Set;

public record ProjectDto(
        Long id,
        String name,
        String description,
        LocalDate startDate,
        LocalDate endDate,
        String status,
        Set<UserDto> users
) {
}
