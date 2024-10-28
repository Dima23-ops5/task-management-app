package com.example.task_management_app.service.internal;

import com.example.task_management_app.dto.attachment.AttachmentDto;
import jakarta.validation.constraints.Positive;
import java.io.InputStream;
import java.util.List;

public interface AttachmentService {
    AttachmentDto uploadFile(Long taskId, String name, InputStream inputStream);

    List<AttachmentDto> findAllByTaskId(@Positive Long taskId);
}
