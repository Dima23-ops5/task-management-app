package com.example.task_management_app.mapper;

import com.example.task_management_app.config.MapperConfig;
import com.example.task_management_app.dto.task.TaskCreateRequestDto;
import com.example.task_management_app.dto.task.TaskDto;
import com.example.task_management_app.dto.task.TaskUpdateRequestDto;
import com.example.task_management_app.model.Project;
import com.example.task_management_app.model.Task;
import com.example.task_management_app.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = LabelMapper.class)
public interface TaskMapper {
    Task toEntity(TaskCreateRequestDto requestDto);

    @Mapping(target = "labelDtos", source = "labels", qualifiedByName = "labelsToDto")
    @Mapping(target = "projectId", source = "project", qualifiedByName = "getProjectId")
    @Mapping(target = "userId", source = "assignee", qualifiedByName = "getUserId")
    TaskDto toDto(Task task);

    Task updateTask(TaskUpdateRequestDto requestDto, @MappingTarget Task task);

    @Named(value = "getProjectId")
    default Long getProjectId(Project project) {
        return project.getId();
    }

    @Named(value = "getUserId")
    default Long getUserId(User user) {
        return user.getId();
    }
}
