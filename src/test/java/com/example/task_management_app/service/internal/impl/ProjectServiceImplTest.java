package com.example.task_management_app.service.internal.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.task_management_app.dto.project.ProjectCreateRequestDto;
import com.example.task_management_app.dto.project.ProjectDto;
import com.example.task_management_app.dto.project.ProjectUpdateRequestDto;
import com.example.task_management_app.mapper.ProjectMapper;
import com.example.task_management_app.model.Project;
import com.example.task_management_app.model.User;
import com.example.task_management_app.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private ProjectMapper projectMapper;
    @InjectMocks
    private ProjectServiceImpl projectService;

    @Test
    @DisplayName(value = "Should save and return correct project")
    public void createProject_Ok() {
        Project project = createTestProject();
        ProjectCreateRequestDto requestDto = new ProjectCreateRequestDto(
                project.getName(), project.getDescription(),
                project.getStartDate(), project.getEndDate(),
                project.getUsers().stream()
                        .map(User::getId)
                        .collect(Collectors.toSet()));
        ProjectDto expected = projectToProjectDto(project);

        when(projectMapper.toEntity(requestDto)).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(project);
        when(projectMapper.toDto(project)).thenReturn(expected);

        ProjectDto actual = projectService.createProject(requestDto);

        assertNotNull(actual);
        assertEquals(expected, actual);

        verify(projectMapper).toEntity(requestDto);
        verify(projectMapper).toDto(project);
    }

    @Test
    @DisplayName(value = "Should return all projects which contains user with id = 1")
    public void findProjectsByUserId_ReturnTwoProjects_Ok() {
        User user = createTestUser();

        Project project1 = createTestProject();
        project1.getUsers().add(user);

        Project project2 = new Project();
        project2.setId(2L);
        project2.setName("Python");
        project2.setDescription("simple game");
        project2.setStartDate(LocalDate.parse("2024-11-03"));
        project2.setEndDate(LocalDate.parse("2024-11-07"));
        project2.setStatus(Project.Status.INITIATED);
        project2.setUsers(Set.of(user));

        ProjectDto projectDto1 = projectToProjectDto(project1);
        ProjectDto projectDto2 = projectToProjectDto(project2);

        List<Project> projects = List.of(project1, project2);

        when(projectRepository.findAllByUserId(user.getId())).thenReturn(projects);
        when(projectMapper.toDto(project1)).thenReturn(projectDto1);
        when(projectMapper.toDto(project2)).thenReturn(projectDto2);

        List<ProjectDto> expected = List.of(projectDto1, projectDto2);
        List<ProjectDto> actual = projectService.findUserProjects(user.getId());

        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertEquals(expected, actual);

        verify(projectRepository).findAllByUserId(user.getId());
        verify(projectMapper).toDto(project1);
        verify(projectMapper).toDto(project2);
    }

    @Test
    @DisplayName(value = "Should return project with id = 1")
    public void findProjectById_Ok() {
        Project project = createTestProject();

        ProjectDto expected = projectToProjectDto(project);

        when(projectRepository.findProjectById(project.getId())).thenReturn(Optional.of(project));
        when(projectMapper.toDto(project)).thenReturn(expected);

        ProjectDto actual = projectService.findProjectById(project.getId());

        assertNotNull(actual);
        assertEquals(expected, actual);

        verify(projectRepository).findProjectById(project.getId());
        verify(projectMapper).toDto(project);
    }

    @Test
    @DisplayName(value = "Verify update project with not existed id,"
            + " should return EntityNotFoundException")
    public void updateProject_NotExistedId_ReturnEntityNotFoundException() {
        Long id = 10L;

        ProjectUpdateRequestDto requestDto = new ProjectUpdateRequestDto(
                "Python", "simple game",
                LocalDate.parse("2024-11-03"), LocalDate.parse("2024-11-07"),
                "INITIATED", new HashSet<>()
        );

        String expectedMessage = "Cannot find project with id: " + id;

        when(projectRepository.findProjectById(id)).thenThrow(
                new EntityNotFoundException("Cannot find project with id: " + id));

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> projectService.updateProjectById(id, requestDto));

        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
    }

    private Project createTestProject() {
        Project project = new Project();
        project.setId(1L);
        project.setName("Car sharing");
        project.setDescription("Service that can help people rent cars");
        project.setStartDate(LocalDate.parse("2024-11-01"));
        project.setEndDate(LocalDate.parse("2024-11-05"));
        project.setStatus(Project.Status.IN_PROGRESS);
        project.setUsers(new HashSet<>());
        return project;
    }

    private ProjectDto projectToProjectDto(Project project) {
        ProjectDto projectDto = new ProjectDto(
                project.getId(), project.getName(),
                project.getDescription(), project.getStartDate(),
                project.getEndDate(), String.valueOf(project.getStatus()),
                new HashSet<>()
        );
        return projectDto;
    }

    private User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setUserName("bob123");
        user.setPassword("Bobpassword");
        user.setEmail("bob@gmail.com");
        user.setFirstName("Bob");
        user.setLastName("Petrenko");
        return user;
    }

}
