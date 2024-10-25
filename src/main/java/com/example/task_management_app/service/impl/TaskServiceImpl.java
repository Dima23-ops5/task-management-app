package com.example.task_management_app.service.impl;

import com.example.task_management_app.dto.task.TaskCreateRequestDto;
import com.example.task_management_app.dto.task.TaskDto;
import com.example.task_management_app.dto.task.TaskUpdateRequestDto;
import com.example.task_management_app.mapper.TaskMapper;
import com.example.task_management_app.model.Label;
import com.example.task_management_app.model.Task;
import com.example.task_management_app.repository.LabelRepository;
import com.example.task_management_app.repository.TaskRepository;
import com.example.task_management_app.repository.UserRepository;
import com.example.task_management_app.service.TaskService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;
    private final LabelRepository labelRepository;

    @Override
    public TaskDto createNewTask(TaskCreateRequestDto requestDto) {
        Task task = taskMapper.toEntity(requestDto);
        task.setAssignee(userRepository.findById(requestDto.userId()).orElseThrow(
                () -> new EntityNotFoundException("Cannot find user with id: " + requestDto.userId())
        ));
        task.setStatus(Task.Status.NOT_STARTED);
        Set<Label> labels = new HashSet<>();
        for (Long id : requestDto.labelIds()) {
            labels.add(labelRepository.findById(id).orElseThrow(
                    () -> new EntityNotFoundException("Cannot find label with id: " + id)
            ));
        }
        task.setLabels(labels);
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    public List<TaskDto> findAllTasksForProject(Long projectId) {
        return taskRepository.findAllByProjectId(projectId).stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Override
    public TaskDto findTaskById(Long id) {
        return taskMapper.toDto(taskRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Cannot find task with id: " + id)
                ));
    }

    @Override
    @Transactional
    public TaskDto updateTaskById(Long id, TaskUpdateRequestDto requestDto) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Cannot find task with id: " + id)
        );
        Task afterUpdating = taskMapper.updateTask(requestDto, task);
        return taskMapper.toDto(taskRepository.save(afterUpdating));
    }

    @Override
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }

}
