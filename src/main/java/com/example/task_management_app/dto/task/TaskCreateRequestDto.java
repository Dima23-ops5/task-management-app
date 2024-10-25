package com.example.task_management_app.dto.task;

import java.time.LocalDate;
import java.util.Set;

public record TaskCreateRequestDto(
        String description,
        String priority,
        LocalDate dueDate,
        Long projectId,
        Long userId,
        Set<Long> labelIds
) {
}
