package com.example.task_management_app.dto.attachment;

import com.example.task_management_app.dto.task.TaskDto;
import java.time.LocalDateTime;

public record AttachmentDto(
        Long id,
        TaskDto taskDto,
        String dropboxFileId,
        String fileName,
        LocalDateTime uploadDate
) {
}
