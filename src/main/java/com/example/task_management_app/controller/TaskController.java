package com.example.task_management_app.controller;

import com.example.task_management_app.dto.task.TaskCreateRequestDto;
import com.example.task_management_app.dto.task.TaskDto;
import com.example.task_management_app.dto.task.TaskUpdateRequestDto;
import com.example.task_management_app.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Task management", description = "Endpoints for managing tasks")
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create task",
            description = "Creating a new task and saving it to database")
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    public TaskDto createTask(@RequestBody @Valid TaskCreateRequestDto requestDto) {
        return taskService.createNewTask(requestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get tasks for project",
            description = "Retrieve and return all tasks for projects")
    @PreAuthorize(value = "hasAuthority('USER')")
    public List<TaskDto> getAllTasks(@RequestParam @Positive Long projectId) {
        return taskService.findAllTasksForProject(projectId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get task by id",
            description = "Retrieve and return task by it's id")
    @PreAuthorize(value = "hasAuthority('USER')")
    public TaskDto getTask(@PathVariable @Positive Long id) {
        return taskService.findTaskById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update task by id",
            description = "Updating and return updated task by it's id")
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    public TaskDto updateTask(@PathVariable @Positive Long id,
                              @RequestBody @Valid TaskUpdateRequestDto requestDto) {
        return taskService.updateTaskById(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete task by id",
            description = "Deleting task by it's id and return no content status")
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    public void deleteTask(@PathVariable @Positive Long id) {
        taskService.deleteTaskById(id);
    }

}
