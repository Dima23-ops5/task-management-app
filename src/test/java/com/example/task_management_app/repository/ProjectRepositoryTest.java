package com.example.task_management_app.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.task_management_app.exception.EntityNotFoundException;
import com.example.task_management_app.model.Project;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:/database/project/add-three-projects.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:/database/project/remove-all-projects.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class ProjectRepositoryTest {
    private static final int FIRST_PROJECT_INDEX = 0;
    private static final int SECOND_PROJECT_INDEX = 1;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    @DisplayName("Returns two projects when finding all projects for user with ID = 1")
    public void findAllProjectsByUserId_ReturnTwoProjects_Ok() {
        Project project1 = new Project();
        project1.setId(1L);
        project1.setName("Car sharing");
        project1.setDescription("Service that can help people rent cars");
        project1.setStartDate(LocalDate.parse("2024-11-01"));
        project1.setEndDate(LocalDate.parse("2024-11-05"));
        project1.setStatus(Project.Status.IN_PROGRESS);

        Project project2 = new Project();
        project2.setId(2L);
        project2.setName("Python");
        project2.setDescription("simple game");
        project2.setStartDate(LocalDate.parse("2024-11-03"));
        project2.setEndDate(LocalDate.parse("2024-11-07"));
        project2.setStatus(Project.Status.INITIATED);

        List<Project> expected = List.of(project1, project2);

        Pageable pageable = PageRequest.of(0, 2);
        List<Project> actual = projectRepository.findAllByUsersId(1L, pageable).stream().toList();

        assertNotNull(actual);
        assertEquals(expected.get(FIRST_PROJECT_INDEX).getId(),
                actual.get(FIRST_PROJECT_INDEX).getId());
        assertEquals(expected.get(FIRST_PROJECT_INDEX).getName(),
                actual.get(FIRST_PROJECT_INDEX).getName());
        assertEquals(expected.get(FIRST_PROJECT_INDEX).getDescription(),
                actual.get(FIRST_PROJECT_INDEX).getDescription());
        assertEquals(expected.get(FIRST_PROJECT_INDEX).getStatus(),
                actual.get(FIRST_PROJECT_INDEX).getStatus());
        assertEquals(expected.get(FIRST_PROJECT_INDEX).getStartDate(),
                actual.get(FIRST_PROJECT_INDEX).getStartDate());
        assertEquals(expected.get(FIRST_PROJECT_INDEX).getEndDate(),
                actual.get(FIRST_PROJECT_INDEX).getEndDate());
        assertEquals(expected.get(SECOND_PROJECT_INDEX).getId(),
                actual.get(SECOND_PROJECT_INDEX).getId());
        assertEquals(expected.get(SECOND_PROJECT_INDEX).getName(),
                actual.get(SECOND_PROJECT_INDEX).getName());
        assertEquals(expected.get(SECOND_PROJECT_INDEX).getDescription(),
                actual.get(SECOND_PROJECT_INDEX).getDescription());
        assertEquals(expected.get(SECOND_PROJECT_INDEX).getStatus(),
                actual.get(SECOND_PROJECT_INDEX).getStatus());
        assertEquals(expected.get(SECOND_PROJECT_INDEX).getStartDate(),
                actual.get(SECOND_PROJECT_INDEX).getStartDate());
        assertEquals(expected.get(SECOND_PROJECT_INDEX).getEndDate(),
                actual.get(SECOND_PROJECT_INDEX).getEndDate());
    }

    @Test
    @DisplayName(value = "Should return correct project by id = 3")
    public void findProjectById_Ok() {
        Long projectId = 3L;

        Project expected = new Project();
        expected.setId(3L);
        expected.setName("To do");
        expected.setDescription("create tasks that need to do");
        expected.setStatus(Project.Status.INITIATED);
        expected.setStartDate(LocalDate.parse("2024-11-02"));
        expected.setEndDate(LocalDate.parse("2024-11-08"));

        Project actual = projectRepository.findProjectById(projectId).get();
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getStatus(), actual.getStatus());
        assertEquals(expected.getStartDate(), actual.getStartDate());
        assertEquals(expected.getEndDate(), actual.getEndDate());
    }

    @Test
    @DisplayName(value = "Should throw EntityNotFoundException when project not found by ID")
    public void findProjectByNotExistedId_ThrowEntityNotFoundException() {
        Long projectId = 4L;
        String expectedMessage = "Cannot find project with id: " + projectId;

        EntityNotFoundException actualException =
                Assertions.assertThrows(EntityNotFoundException.class,
                        () -> projectRepository.findProjectById(projectId).orElseThrow(
                                () -> new EntityNotFoundException("Cannot find project with id: "
                                + projectId)
                ));

        assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    @DisplayName(value = "Should return all project with end date")
    public void findAllProjectsByEndDate_Ok() {
        LocalDate endDate = LocalDate.parse("2024-11-07");

        Project project = new Project();
        project.setId(2L);
        project.setName("Python");
        project.setDescription("simple game");
        project.setStartDate(LocalDate.parse("2024-11-03"));
        project.setEndDate(LocalDate.parse("2024-11-07"));
        project.setStatus(Project.Status.INITIATED);

        List<Project> expected = List.of(project);
        List<Project> actual = projectRepository.findAllByEndDate(endDate);

        assertNotNull(actual);
        assertEquals(expected.get(FIRST_PROJECT_INDEX).getId(),
                actual.get(FIRST_PROJECT_INDEX).getId());
        assertEquals(expected.get(FIRST_PROJECT_INDEX).getName(),
                actual.get(FIRST_PROJECT_INDEX).getName());
        assertEquals(expected.get(FIRST_PROJECT_INDEX).getDescription(),
                actual.get(FIRST_PROJECT_INDEX).getDescription());
        assertEquals(expected.get(FIRST_PROJECT_INDEX).getStartDate(),
                actual.get(FIRST_PROJECT_INDEX).getStartDate());
        assertEquals(expected.get(FIRST_PROJECT_INDEX).getEndDate(),
                actual.get(FIRST_PROJECT_INDEX).getEndDate());
        assertEquals(expected.get(FIRST_PROJECT_INDEX).getStatus(),
                actual.get(FIRST_PROJECT_INDEX).getStatus());
    }

}
