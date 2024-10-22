package com.example.task_management_app.controller;

import com.example.task_management_app.dto.project.ProjectCreateRequestDto;
import com.example.task_management_app.dto.project.ProjectDto;
import com.example.task_management_app.dto.project.ProjectUpdateRequestDto;
import com.example.task_management_app.model.User;
import com.example.task_management_app.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Project management", description = "Endpoints for managing projects")
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new project",
            description = "Creating new project and save it to database")
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    public ProjectDto createNewProject(@RequestBody @Valid ProjectCreateRequestDto requestDto) {
        return projectService.createProject(requestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all user's projects",
            description = "Retrieve and return all projects the currently authenticated user.")
    @PreAuthorize(value = "hasAuthority('USER')")
    public List<ProjectDto> getAllUsersProjects(Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        return projectService.findUserProjects(userId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get project by id",
            description = "Retrieve and return project by it's id")
    @PreAuthorize(value = "hasAuthority('USER')")
    public ProjectDto getProjectById(@PathVariable @Positive Long id) {
        return projectService.findProjectById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update project by id",
            description = "Update and return updated project by it's id")
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    public ProjectDto updateProjectById(@PathVariable Long id,
                                        @RequestBody ProjectUpdateRequestDto requestDto) {
        return projectService.updateProjectById(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete project by id",
            description = "Delete project by it's id and return no content status")
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    public void deleteProjectById(@PathVariable @Positive Long id) {
        projectService.deleteProjectById(id);
    }

    private Long getCurrentUserId(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }

}
