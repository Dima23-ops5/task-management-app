package com.example.task_management_app.dto.label;

import jakarta.validation.constraints.NotBlank;

public record LabelUpdateRequestDto(
        @NotBlank(message = "Label name can't be null") String name,
        @NotBlank(message = "Color can't be null") String color
) {
}
