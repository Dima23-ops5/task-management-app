package com.example.task_management_app.service;

import com.example.task_management_app.dto.task.TaskCreateRequestDto;
import com.example.task_management_app.dto.task.TaskDto;
import com.example.task_management_app.dto.task.TaskUpdateRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;

public interface TaskService {
    TaskDto createNewTask(TaskCreateRequestDto requestDto);

    List<TaskDto> findAllTasksForProject(Long projectId);

    TaskDto findTaskById(Long id);

    TaskDto updateTaskById(@Positive Long id, @Valid TaskUpdateRequestDto requestDto);

    void deleteTaskById(@Positive Long id);
}
