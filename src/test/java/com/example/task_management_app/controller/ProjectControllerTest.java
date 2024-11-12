package com.example.task_management_app.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.task_management_app.dto.project.ProjectCreateRequestDto;
import com.example.task_management_app.dto.project.ProjectDto;
import com.example.task_management_app.dto.project.ProjectUpdateRequestDto;
import com.example.task_management_app.dto.user.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProjectControllerTest {
    @Autowired
    protected static MockMvc mockMvc;

    private static final int FIRST_PROJECT_INDEX = 0;
    private static final int SECOND_PROJECT_INDEX = 1;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private JavaMailSender mailSender;

    @BeforeAll
    static void setUpBeforeClass(@Autowired DataSource dataSource,
                                 @Autowired WebApplicationContext webApplicationContext)
            throws SQLException {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("/database/project/add-all-information-for-testing.sql"));
        }
    }

    @AfterAll
    static void setUpAfterClass(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(@Autowired DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("/database/project/remove-all.sql"));
        }
    }

    @Test
    @DisplayName(value = "Create a New Project - Success")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "/database/project/delete-created-project.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createNewProject() throws Exception {
        ProjectCreateRequestDto requestDto = new ProjectCreateRequestDto("Project Alpha",
                "First phase of the Alpha project focused on research and planning",
                LocalDate.parse("2024-01-01"), LocalDate.parse("2024-06-01"),
                new HashSet<>());

        ProjectDto expected = new ProjectDto(5L, requestDto.name(),
                requestDto.description(), requestDto.startDate(),
                requestDto.endDate(), "INITIATED",
                new HashSet<>());

        String requestJson = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(post("/api/projects").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        ProjectDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                ProjectDto.class);

        assertNotNull(actual);
        assertEquals(expected, actual);;
    }

    @Test
    @DisplayName(value = "Retrieve All Projects Containing User"
            + " with ID = 1 - Expected Two Projects")
    @WithUserDetails("admin@example.com")
    public void getAllProjects_ReturnTwoProjects_Ok() throws Exception {
        UserDto user1 = new UserDto(1L, "admin",
                "Admin", "Administrator",
                "admin@example.com");

        ProjectDto project1 = new ProjectDto(1L, "Car sharing",
                "Service that can help people rent cars",
                LocalDate.parse("2024-11-01"), LocalDate.parse("2024-11-05"),
                "IN_PROGRESS", Set.of(user1));

        UserDto user2 = new UserDto(2L, "asmith", "Alice", "Smith", "asmith@example.com");

        ProjectDto project2 = new ProjectDto(2L, "Python",
                "simple game", LocalDate.parse("2024-11-03"),
                LocalDate.parse("2024-11-07"),
                "INITIATED", Set.of(user1, user2));

        List<ProjectDto> expected = List.of(project1, project2);

        MvcResult result = mockMvc.perform(get("/api/projects")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<ProjectDto> actual = Arrays.stream(objectMapper.readValue(
                result.getResponse().getContentAsString(), ProjectDto[].class))
                .sorted(Comparator.comparingLong(ProjectDto::id))
                .toList();

        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(FIRST_PROJECT_INDEX), actual.get(FIRST_PROJECT_INDEX));
        assertEquals(expected.get(SECOND_PROJECT_INDEX), actual.get(SECOND_PROJECT_INDEX));
    }

    @Test
    @DisplayName(value = "Retrieve Project by ID - Success")
    @WithMockUser(username = "user", roles = {"USER"})
    public void getProjectById_ReturnProject_Ok() throws Exception {
        UserDto user1 = new UserDto(1L, "admin",
                "Admin", "Administrator",
                "admin@example.com");
        UserDto user2 = new UserDto(2L, "asmith",
                "Alice", "Smith",
                "asmith@example.com");

        ProjectDto expected = new ProjectDto(2L, "Python",
                "simple game", LocalDate.parse("2024-11-03"),
                LocalDate.parse("2024-11-07"),
                "INITIATED", Set.of(user1, user2));

        MvcResult result = mockMvc.perform(get("/api/projects/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ProjectDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                ProjectDto.class);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName(value = "Update Project - Success")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateProject() throws Exception {
        ProjectUpdateRequestDto requestDto = new ProjectUpdateRequestDto("Project Beta",
                "Development and testing phase for the Beta product line",
                LocalDate.parse("2024-02-15"), LocalDate.parse("2024-08-15"),
                "IN_PROGRESS", new HashSet<>());

        ProjectDto expected = new ProjectDto(3L, requestDto.name(),
                requestDto.description(), requestDto.startDate(),
                requestDto.endDate(), requestDto.status(),
                new HashSet<>());

        String requestJson = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(put("/api/projects/3")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ProjectDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                ProjectDto.class);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName(value = "Delete Project - Success (No Content Returned)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteProject_AndReturnNoContent_Ok() throws Exception {
        MvcResult result = mockMvc.perform(delete("/api/projects/4")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        int actual = result.getResponse().getStatus();
        assertEquals(204, actual);
    }

}
