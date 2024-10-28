package com.example.task_management_app.dto.comment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CommentCreateRequestDto(
        @NotNull Long taskId,
        @NotEmpty String text
) {
}
