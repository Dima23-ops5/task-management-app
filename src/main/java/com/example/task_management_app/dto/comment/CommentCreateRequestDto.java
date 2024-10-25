package com.example.task_management_app.dto.comment;

public record CommentCreateRequestDto(
        Long taskId,
        String text
) {
}
