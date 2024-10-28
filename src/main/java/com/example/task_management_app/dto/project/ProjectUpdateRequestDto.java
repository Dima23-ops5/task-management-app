package com.example.task_management_app.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

public record ProjectUpdateRequestDto(
        @NotBlank(message = "Name can't be null") String name,
        @NotBlank(message = "Description can't be null") String description,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        @NotBlank(message = "Status can't be null") String status,
        Set<Long> usersId
) {
}
