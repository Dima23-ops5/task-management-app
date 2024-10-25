package com.example.task_management_app.mapper;

import com.example.task_management_app.config.MapperConfig;
import com.example.task_management_app.dto.task.TaskCreateRequestDto;
import com.example.task_management_app.dto.task.TaskDto;
import com.example.task_management_app.dto.task.TaskUpdateRequestDto;
import com.example.task_management_app.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = LabelMapper.class)
public interface TaskMapper {
    Task toEntity(TaskCreateRequestDto requestDto);

    @Mapping(target = "labelDtos", source = "labels", qualifiedByName = "labelsToDto")
    TaskDto toDto(Task task);

    Task updateTask(TaskUpdateRequestDto requestDto, @MappingTarget Task task);
}
