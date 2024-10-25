package com.example.task_management_app.dto.task;

import java.time.LocalDate;
import java.util.Set;

public record TaskUpdateRequestDto(
        String description,
        String priority,
        String status,
        LocalDate dueDate,
        Long projectId,
        Long userId,
        Set<Long> labelDto
) {
}
