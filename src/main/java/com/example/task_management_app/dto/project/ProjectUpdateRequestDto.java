package com.example.task_management_app.dto.project;

import java.time.LocalDate;
import java.util.Set;

public record ProjectUpdateRequestDto(
        String name,
        String description,
        LocalDate startDate,
        LocalDate endDate,
        String status,
        Set<Long> usersId
) {
}
