package com.example.task_management_app.dto.project;

import java.time.LocalDate;
import java.util.Set;

public record ProjectCreateRequestDto(
        String name,
        String description,
        LocalDate startDate,
        LocalDate endDate,
        Set<Long> usersId
) {
}
