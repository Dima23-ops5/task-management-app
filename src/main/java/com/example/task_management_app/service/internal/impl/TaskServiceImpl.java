package com.example.task_management_app.service.internal.impl;

import com.example.task_management_app.dto.task.TaskCreateRequestDto;
import com.example.task_management_app.dto.task.TaskDto;
import com.example.task_management_app.dto.task.TaskUpdateRequestDto;
import com.example.task_management_app.mapper.TaskMapper;
import com.example.task_management_app.model.Label;
import com.example.task_management_app.model.Project;
import com.example.task_management_app.model.Task;
import com.example.task_management_app.model.User;
import com.example.task_management_app.repository.LabelRepository;
import com.example.task_management_app.repository.ProjectRepository;
import com.example.task_management_app.repository.TaskRepository;
import com.example.task_management_app.repository.UserRepository;
import com.example.task_management_app.service.internal.TaskService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;
    private final LabelRepository labelRepository;
    private final ProjectRepository projectRepository;

    @Override
    public TaskDto createNewTask(TaskCreateRequestDto requestDto) {
        Task task = taskMapper.toEntity(requestDto);
        task.setProject(projectRepository.findById(requestDto.projectId()).orElseThrow(
                () -> new EntityNotFoundException("Connot find project by id: "
                        + requestDto.projectId())
        ));
        task.setAssignee(userRepository.findById(requestDto.userId()).orElseThrow(
                () -> new EntityNotFoundException("Cannot find user with id: "
                        + requestDto.userId())
        ));
        task.setStatus(Task.Status.NOT_STARTED);
        taskRepository.save(task);
        Set<Label> labels = getLabelByIds(requestDto.labelIds());
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
        Project project = projectRepository.findProjectById(requestDto.projectId()).orElseThrow(
                () -> new EntityNotFoundException("Cannot find project with id: "
                        + requestDto.projectId()));
        User user = userRepository.findUserById(requestDto.userId()).orElseThrow(
                () -> new EntityNotFoundException("Cannot find user with id: "
                        + requestDto.userId()));
        Task afterUpdating = taskMapper.updateTask(requestDto, task);
        task.setProject(project);
        task.setAssignee(user);
        Set<Label> labels = getLabelByIds(requestDto.labelIds());
        afterUpdating.setLabels(labels);
        return taskMapper.toDto(taskRepository.save(afterUpdating));
    }

    @Override
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }

    private Set<Label> getLabelByIds(Set<Long> labelIds) {
        if (labelIds == null) {
            return Collections.emptySet();
        }
        return labelIds.stream()
                .map(id -> labelRepository.findById(id).orElseThrow(
                        () -> new EntityNotFoundException("Cannot find label with id: " + id)
                ))
                .collect(Collectors.toSet());
    }

}
