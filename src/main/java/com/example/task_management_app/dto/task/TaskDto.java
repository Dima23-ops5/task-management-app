package com.example.task_management_app.dto.task;

import com.example.task_management_app.dto.label.LabelDto;
import java.time.LocalDate;
import java.util.Set;

public record TaskDto(
        Long id,
        String name,
        String description,
        String priority,
        String status,
        LocalDate dueDate,
        Long projectId,
        Long userId,
        Set<LabelDto> labelDtos
) {
}
