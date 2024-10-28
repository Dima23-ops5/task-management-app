package com.example.task_management_app.mapper;

import com.example.task_management_app.config.MapperConfig;
import com.example.task_management_app.dto.attachment.AttachmentDto;
import com.example.task_management_app.model.Attachment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = TaskMapper.class)
public interface AttachmentMapper {
    @Mapping(source = "task", target = "taskDto", qualifiedByName = "taskToDto")
    AttachmentDto toDto(Attachment attachment);
}
