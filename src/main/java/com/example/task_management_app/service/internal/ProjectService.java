package com.example.task_management_app.service.internal;

import com.example.task_management_app.dto.project.ProjectCreateRequestDto;
import com.example.task_management_app.dto.project.ProjectDto;
import com.example.task_management_app.dto.project.ProjectUpdateRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectService {
    ProjectDto createProject(@Valid ProjectCreateRequestDto requestDto);

    Page<ProjectDto> findUserProjects(Long userId, Pageable pageable);

    ProjectDto findProjectById(@Positive Long id);

    ProjectDto updateProjectById(Long id, ProjectUpdateRequestDto requestDto);

    void deleteProjectById(@Positive Long id);
}
