package com.example.task_management_app.service.impl;

import com.example.task_management_app.dto.project.ProjectCreateRequestDto;
import com.example.task_management_app.dto.project.ProjectDto;
import com.example.task_management_app.dto.project.ProjectUpdateRequestDto;
import com.example.task_management_app.mapper.ProjectMapper;
import com.example.task_management_app.model.Project;
import com.example.task_management_app.model.User;
import com.example.task_management_app.repository.ProjectRepository;
import com.example.task_management_app.repository.UserRepository;
import com.example.task_management_app.service.ProjectService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ProjectDto createProject(ProjectCreateRequestDto requestDto) {
        Project project = projectMapper.toEntity(requestDto);
        project.setStatus(Project.Status.INITIATED);
        projectRepository.save(project);
        project.setUsers(getUsers(requestDto.usersId()));
        return projectMapper.toDto(projectRepository.save(project));
    }

    @Override
    public List<ProjectDto> findUserProjects(Long userId) {
        return projectRepository.findAllByUserId(userId).stream()
                .map(projectMapper::toDto)
                .toList();
    }

    @Override
    public ProjectDto findProjectById(Long id) {
        Project project = projectRepository.findProjectById(id).orElseThrow(
                () -> new EntityNotFoundException("Cannot find project with id: " + id)
        );
        return projectMapper.toDto(project);
    }

    @Override
    public ProjectDto updateProjectById(Long id, ProjectUpdateRequestDto requestDto) {
        Project project = projectRepository.findProjectById(id).orElseThrow(
                () -> new EntityNotFoundException("Cannot find project with id: " + id)
        );
        Project afterUpdating = projectMapper.updateEntity(requestDto, project);
        afterUpdating.setUsers(getUsers(requestDto.usersId()));
        return projectMapper.toDto(projectRepository.save(afterUpdating));
    }

    @Override
    public void deleteProjectById(Long id) {
        projectRepository.deleteById(id);
    }

    private Set<User> getUsers(Set<Long> userIds) {
        Set<User> users = new HashSet<>();
        for (Long userId : userIds) {
            users.add(userRepository.findUserById(userId).orElseThrow(
                    () -> new EntityNotFoundException("Cannot find user with id: " + userId)));
        }
        return users;
    }
}
