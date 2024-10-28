package com.example.task_management_app.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

public record TaskCreateRequestDto(
        @NotBlank(message = "Description can't be null") String description,
        @NotBlank(message = "Priority can't be null") String priority,
        @NotNull LocalDate dueDate,
        @NotNull(message = "Task must contains project") Long projectId,
        @NotNull(message = "Task must have user") Long userId,
        Set<Long> labelIds
) {
}
