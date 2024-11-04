package com.example.task_management_app.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.task_management_app.model.Task;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:/database/project/add-three-projects.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:/database/task/add-three-tasks.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:/database/task/remove-all-tasks.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
@Sql(scripts = "classpath:/database/project/remove-all-projects.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class TaskRepositoryTest {
    private static final int FIRST_TASK_INDEX = 0;
    private static final int SECOND_TASK_INDEX = 1;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    @DisplayName(value = "Should return two tasks by project with id = 2")
    public void findAllTasksByProjectId_Ok() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setName("Develop Game Logic");
        task1.setDescription("Implement the core game mechanics and rules");
        task1.setPriority(Task.Priority.HIGH);
        task1.setStatus(Task.Status.IN_PROCESS);
        task1.setDueDate(LocalDate.parse("2024-11-05"));

        Task task2 = new Task();
        task2.setId(2L);
        task2.setName("Design Game UI");
        task2.setDescription("Create a simple user interface for the game");
        task2.setPriority(Task.Priority.MEDIUM);
        task2.setStatus(Task.Status.NOT_STARTED);
        task2.setDueDate(LocalDate.parse("2024-11-06"));

        Long projectId = 2L;

        List<Task> expected = List.of(task1, task2);
        List<Task> actual = taskRepository.findAllByProjectId(projectId);

        assertNotNull(actual);
        assertEquals(expected.get(FIRST_TASK_INDEX).getId(),
                actual.get(FIRST_TASK_INDEX).getId());
        assertEquals(expected.get(FIRST_TASK_INDEX).getName(),
                actual.get(FIRST_TASK_INDEX).getName());
        assertEquals(expected.get(FIRST_TASK_INDEX).getDescription(),
                actual.get(FIRST_TASK_INDEX).getDescription());
        assertEquals(expected.get(FIRST_TASK_INDEX).getPriority(),
                actual.get(FIRST_TASK_INDEX).getPriority());
        assertEquals(expected.get(FIRST_TASK_INDEX).getStatus(),
                actual.get(FIRST_TASK_INDEX).getStatus());
        assertEquals(expected.get(FIRST_TASK_INDEX).getDueDate(),
                actual.get(FIRST_TASK_INDEX).getDueDate());
        assertEquals(expected.get(SECOND_TASK_INDEX).getId(),
                actual.get(SECOND_TASK_INDEX).getId());
        assertEquals(expected.get(SECOND_TASK_INDEX).getName(),
                actual.get(SECOND_TASK_INDEX).getName());
        assertEquals(expected.get(SECOND_TASK_INDEX).getDescription(),
                actual.get(SECOND_TASK_INDEX).getDescription());
        assertEquals(expected.get(SECOND_TASK_INDEX).getPriority(),
                actual.get(SECOND_TASK_INDEX).getPriority());
        assertEquals(expected.get(SECOND_TASK_INDEX).getStatus(),
                actual.get(SECOND_TASK_INDEX).getStatus());
        assertEquals(expected.get(SECOND_TASK_INDEX).getDueDate(),
                actual.get(SECOND_TASK_INDEX).getDueDate());
    }

    @Test
    @DisplayName(value = "Should return all task with due date")
    public void findAllTasksWithDueDate_Ok() {
        LocalDate dueDate = LocalDate.parse("2024-11-07");

        Task task = new Task();
        task.setId(3L);
        task.setName("Testing and Bug Fixing");
        task.setDescription("Test the app and resolve any bugs");
        task.setPriority(Task.Priority.HIGH);
        task.setStatus(Task.Status.IN_PROCESS);
        task.setDueDate(LocalDate.parse("2024-11-07"));

        List<Task> expected = List.of(task);
        List<Task> actual = taskRepository.findAllByDueDate(dueDate);

        assertNotNull(actual);
        assertEquals(expected.get(FIRST_TASK_INDEX).getId(),
                actual.get(FIRST_TASK_INDEX).getId());
        assertEquals(expected.get(FIRST_TASK_INDEX).getName(),
                actual.get(FIRST_TASK_INDEX).getName());
        assertEquals(expected.get(FIRST_TASK_INDEX).getDescription(),
                actual.get(FIRST_TASK_INDEX).getDescription());
        assertEquals(expected.get(FIRST_TASK_INDEX).getPriority(),
                actual.get(FIRST_TASK_INDEX).getPriority());
        assertEquals(expected.get(FIRST_TASK_INDEX).getStatus(),
                actual.get(FIRST_TASK_INDEX).getStatus());
        assertEquals(expected.get(FIRST_TASK_INDEX).getDueDate(),
                actual.get(FIRST_TASK_INDEX).getDueDate());
    }
}
