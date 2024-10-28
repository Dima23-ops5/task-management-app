package com.example.task_management_app.mapper;

import com.example.task_management_app.config.MapperConfig;
import com.example.task_management_app.dto.project.ProjectCreateRequestDto;
import com.example.task_management_app.dto.project.ProjectDto;
import com.example.task_management_app.dto.project.ProjectUpdateRequestDto;
import com.example.task_management_app.model.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = UserMapper.class)
public interface ProjectMapper {
    Project toEntity(ProjectCreateRequestDto requestDto);

    @Mapping(target = "users", source = "users", qualifiedByName = "usersToUserDtos")
    ProjectDto toDto(Project project);

    Project updateEntity(ProjectUpdateRequestDto requestDto, @MappingTarget Project project);
}
