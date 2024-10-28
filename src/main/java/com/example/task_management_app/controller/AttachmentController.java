package com.example.task_management_app.controller;

import com.example.task_management_app.dto.attachment.AttachmentDto;
import com.example.task_management_app.service.internal.AttachmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Attachment management",
        description = "Endpoints for managing attachments")
@RestController
@RequestMapping("/api/attachments")
@RequiredArgsConstructor
public class AttachmentController {
    private final AttachmentService attachmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Upload file to Dropbox",
            description = "Upload file to Dropbox and return attachment with dropbox file data")
    @PreAuthorize(value = "hasAuthority('USER')")
    public AttachmentDto uploadFile(@RequestParam("taskId") @Positive Long taskId,
                                    @RequestParam("file")MultipartFile file) throws IOException {
        return attachmentService.uploadFile(taskId, file.getName(), file.getInputStream());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all attachments by task id",
            description = "Retrieve and return all actual files by task id")
    @PreAuthorize(value = "hasAuthority('USER')")
    public List<AttachmentDto> getAllAttachments(@RequestParam("taskId") @Positive Long taskId) {
        return attachmentService.findAllByTaskId(taskId);
    }
}
