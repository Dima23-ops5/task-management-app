package com.example.task_management_app.service.internal.impl;

import com.dropbox.core.DbxException;
import com.example.task_management_app.dto.attachment.AttachmentDto;
import com.example.task_management_app.exception.DataProcessingException;
import com.example.task_management_app.mapper.AttachmentMapper;
import com.example.task_management_app.model.Attachment;
import com.example.task_management_app.model.Task;
import com.example.task_management_app.repository.AttachmentRepository;
import com.example.task_management_app.repository.TaskRepository;
import com.example.task_management_app.service.external.DropboxService;
import com.example.task_management_app.service.internal.AttachmentService;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final AttachmentMapper attachmentMapper;
    private final TaskRepository taskRepository;
    private final DropboxService dropboxService;

    @Override
    public AttachmentDto uploadFile(Long taskId, String name, InputStream inputStream) {
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException("Cannot find task with id: " + taskId)
        );
        Attachment attachment = new Attachment();
        try {
            attachment.setDropboxFileId(dropboxService.uploadFile(name, inputStream));
            attachment.setTask(task);
            attachment.setFileName(name);
            attachment.setUploadDate(LocalDateTime.now());
        } catch (IOException | DbxException e) {
            throw new DataProcessingException("Cannot upload file", e);
        }
        return attachmentMapper.toDto(attachmentRepository.save(attachment));
    }

    @Override
    public List<AttachmentDto> findAllByTaskId(Long taskId) {
        return attachmentRepository.findAllByTaskId(taskId).stream()
                .map(attachmentMapper::toDto)
                .toList();
    }
}
