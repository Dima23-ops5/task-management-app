package com.example.task_management_app.service.internal.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.task_management_app.dto.task.TaskCreateRequestDto;
import com.example.task_management_app.dto.task.TaskDto;
import com.example.task_management_app.dto.task.TaskUpdateRequestDto;
import com.example.task_management_app.mapper.TaskMapper;
import com.example.task_management_app.model.Project;
import com.example.task_management_app.model.Task;
import com.example.task_management_app.model.User;
import com.example.task_management_app.repository.ProjectRepository;
import com.example.task_management_app.repository.TaskRepository;
import com.example.task_management_app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskMapper taskMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProjectRepository projectRepository;
    @InjectMocks
    private TaskServiceImpl taskServiceImpl;

    @Test
    @DisplayName(value = "Should create and return new task")
    public void createTask_Ok() {
        User user = createTestUser();
        Project project = new Project();
        Task task = createTestTask(project, user);
        TaskCreateRequestDto requestDto = new TaskCreateRequestDto(task.getName(),
                task.getDescription(), String.valueOf(task.getPriority()), task.getDueDate(),
                task.getProject().getId(), task.getAssignee().getId(),
                new HashSet<>());

        TaskDto expected = taskToTaskDto(task);

        when(taskMapper.toEntity(requestDto)).thenReturn(task);
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(expected);

        TaskDto actual = taskServiceImpl.createNewTask(requestDto);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName(value = "Should return all task that contains project id = 1")
    public void findAllTasksByProjectId_ReturnTwoTasks_Ok() {
        Project project = createTestProject();
        User user = createTestUser();
        Task task1 = createTestTask(project, user);

        Task task2 = new Task();
        task2.setId(2L);
        task2.setName("Design Game UI");
        task2.setDescription("Create a simple user interface for the game");
        task2.setPriority(Task.Priority.MEDIUM);
        task2.setDueDate(LocalDate.parse("2024-11-06"));
        task2.setProject(project);
        task2.setAssignee(user);

        TaskDto taskDto1 = taskToTaskDto(task1);
        TaskDto taskDto2 = taskToTaskDto(task2);

        List<Task> tasks = List.of(task1, task2);

        when(taskRepository.findAllByProjectId(project.getId())).thenReturn(tasks);
        when(taskMapper.toDto(task1)).thenReturn(taskDto1);
        when(taskMapper.toDto(task2)).thenReturn(taskDto2);

        List<TaskDto> actual = taskServiceImpl.findAllTasksForProject(project.getId());
        List<TaskDto> expected = List.of(taskDto1, taskDto2);

        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertEquals(expected, actual);

        verify(taskRepository).findAllByProjectId(project.getId());
        verify(taskMapper).toDto(task1);
        verify(taskMapper).toDto(task2);
    }

    @Test
    @DisplayName(value = "Should return task by it's id")
    public void findTaskById_Ok() {
        User user = createTestUser();
        Project project = createTestProject();
        Task task = createTestTask(project, user);

        TaskDto expected = taskToTaskDto(task);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskMapper.toDto(task)).thenReturn(expected);

        TaskDto actual = taskServiceImpl.findTaskById(task.getId());

        assertNotNull(actual);
        assertEquals(expected, actual);

        verify(taskRepository).findById(task.getId());
        verify(taskMapper).toDto(task);
    }

    @Test
    @DisplayName(value = "Should update and return updated task by id")
    public void updateTaskById_Ok() {
        User user = createTestUser();
        Project project = createTestProject();
        Task task = createTestTask(project, user);

        TaskUpdateRequestDto requestDto = new TaskUpdateRequestDto(
                "Design Game UI", "Create a simple user interface for the game",
                "HIGH", "NOT_STARTED", LocalDate.parse("2024-11-06"),
                project.getId(), user.getId(), new HashSet<>()
        );

        Task expected = new Task();
        expected.setId(task.getId());
        expected.setName(requestDto.name());
        expected.setDescription(requestDto.description());
        expected.setPriority(Task.Priority.HIGH);
        expected.setDueDate(requestDto.dueDate());
        expected.setProject(project);
        expected.setAssignee(user);

        TaskDto expectedDto = taskToTaskDto(expected);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskMapper.updateTask(requestDto, task)).thenReturn(expected);
        when(taskRepository.save(expected)).thenReturn(expected);
        when(taskMapper.toDto(expected)).thenReturn(expectedDto);

        TaskDto actual = taskServiceImpl.updateTaskById(task.getId(), requestDto);

        assertNotNull(actual);
        assertEquals(expectedDto, actual);

        verify(taskRepository).findById(task.getId());
        verify(taskRepository).save(expected);
        verify(taskMapper).toDto(expected);
    }

    @Test
    @DisplayName(value = "Verify not existed id and return EntityNotFoundException")
    public void findTaskById_NotExistedId_ReturnEntityNotFoundException() {
        Long id = 10L;
        String expected = "Cannot find task with id:" + id;

        when(taskRepository.findById(id)).thenThrow(
                new EntityNotFoundException("Cannot find task with id:" + id));

        EntityNotFoundException actual = Assertions.assertThrows(
                EntityNotFoundException.class, () -> taskServiceImpl.findTaskById(id));
        assertEquals(expected, actual.getMessage());
    }

    private Task createTestTask(Project project, User user) {
        Task task = new Task();
        task.setId(1L);
        task.setName("Develop Game Logic");
        task.setDescription("Implement the core game mechanics and rules");
        task.setPriority(Task.Priority.HIGH);
        task.setDueDate(LocalDate.parse("2024-11-05"));
        task.setProject(project);
        task.setAssignee(user);
        return task;
    }

    private Project createTestProject() {
        Project project = new Project();
        project.setId(1L);
        project.setName("Python");
        project.setDescription("simple game");
        project.setStartDate(LocalDate.parse("2024-11-03"));
        project.setEndDate(LocalDate.parse("2024-11-07"));
        project.setStatus(Project.Status.INITIATED);
        return project;
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

    private TaskDto taskToTaskDto(Task task) {
        TaskDto taskDto = new TaskDto(
                task.getId(), task.getName(), task.getDescription(),
                String.valueOf(task.getPriority()), String.valueOf(task.getStatus()),
                task.getDueDate(), task.getProject().getId(),
                task.getAssignee().getId(), new HashSet<>()
        );
        return taskDto;
    }

}
