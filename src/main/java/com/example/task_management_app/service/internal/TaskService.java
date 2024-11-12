package com.example.task_management_app.service.internal;

import com.example.task_management_app.dto.task.TaskCreateRequestDto;
import com.example.task_management_app.dto.task.TaskDto;
import com.example.task_management_app.dto.task.TaskUpdateRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    TaskDto createNewTask(TaskCreateRequestDto requestDto);

    List<TaskDto> findAllTasksForProject(Long projectId, Pageable pageable);

    TaskDto findTaskById(Long id);

    TaskDto updateTaskById(@Positive Long id, @Valid TaskUpdateRequestDto requestDto);

    void deleteTaskById(@Positive Long id);
}
