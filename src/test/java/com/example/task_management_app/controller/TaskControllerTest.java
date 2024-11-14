package com.example.task_management_app.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.task_management_app.dto.task.TaskCreateRequestDto;
import com.example.task_management_app.dto.task.TaskDto;
import com.example.task_management_app.dto.task.TaskUpdateRequestDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerTest {
    @Autowired
    protected static MockMvc mockMvc;
    private static final int FIRST_TASK_INDEX = 0;
    private static final int SECOND_TASK_INDEX = 1;
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
                    new ClassPathResource("/database/task/add-all-information-for-testing.sql"));
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
                    new ClassPathResource("/database/task/remove-all.sql"));
        }
    }

    @Test
    @DisplayName(value = "Create a New Task - Success")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void createTask_Ok() throws Exception {
        TaskCreateRequestDto requestDto = new TaskCreateRequestDto("Database Optimization",
                "Optimize database queries for faster response times.",
                "LOW",
                LocalDate.now(),
                1L,
                2L,
                new HashSet<>()
        );

        TaskDto expected = new TaskDto(5L, requestDto.name(),
                requestDto.description(), requestDto.priority(),
                "NOT_STARTED", requestDto.dueDate(),
                requestDto.projectId(), requestDto.userId(),
                new HashSet<>());

        String requestJson = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/api/tasks")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        TaskDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                TaskDto.class);

        assertNotNull(actual);
        assertEquals(expected.id(), actual.id());
        assertEquals(expected.name(), actual.name());
        assertEquals(expected.description(), actual.description());
        assertEquals(expected.priority(), actual.priority());
        assertEquals(expected.status(), actual.status());
        assertEquals(expected.dueDate(), actual.dueDate());
        assertEquals(expected.projectId(), actual.projectId());
        assertEquals(expected.userId(), actual.userId());
    }

    @Test
    @DisplayName(value = "Retrieve All Tasks Containing Project with ID = 2 - Expected Two Tasks")
    @WithMockUser(username = "user", roles = {"USER"})
    public void getAllTasks_ReturnTwoTasks_Ok() throws Exception {
        TaskDto task1 = new TaskDto(1L, "Develop Game Logic",
                "Implement the core game mechanics and rules",
                "HIGH", "IN_PROCESS", LocalDate.parse("2024-11-05"),
                2L, 1L, new HashSet<>());

        TaskDto task2 = new TaskDto(2L, "Design Game UI",
                "Create a simple user interface for the game",
                "MEDIUM", "NOT_STARTED", LocalDate.parse("2024-11-06"),
                2L, 2L, new HashSet<>());

        List<TaskDto> expected = List.of(task1, task2);

        MvcResult result = mockMvc.perform(get("/api/tasks")
                        .param("projectId", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        List<TaskDto> actual = objectMapper.convertValue(
                root.path("content"), new TypeReference<List<TaskDto>>() {});

        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(FIRST_TASK_INDEX), actual.get(FIRST_TASK_INDEX));
        assertEquals(expected.get(SECOND_TASK_INDEX), actual.get(SECOND_TASK_INDEX));
    }

    @Test
    @DisplayName(value = "Retrieve Task by ID - Success")
    @WithMockUser(username = "user", roles = {"USER"})
    public void getTaskById_Ok() throws Exception {
        TaskDto expected = new TaskDto(3L, "Testing and Bug Fixing",
                "Test the app and resolve any bugs", "HIGH",
                "IN_PROCESS", LocalDate.parse("2024-11-07"),
                3L, 2L, new HashSet<>());

        MvcResult result = mockMvc.perform(get("/api/tasks/3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        TaskDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                TaskDto.class);

        assertNotNull(actual);
        assertEquals(expected.id(), actual.id());
        assertEquals(expected.name(), actual.name());
        assertEquals(expected.description(), actual.description());
        assertEquals(expected.priority(), actual.priority());
        assertEquals(expected.status(), actual.status());
        assertEquals(expected.dueDate(), actual.dueDate());
        assertEquals(expected.projectId(), actual.projectId());
        assertEquals(expected.userId(), actual.userId());
    }

    @Test
    @DisplayName(value = "Update Task - Success")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateTaskById_Ok() throws Exception {
        TaskUpdateRequestDto requestDto = new TaskUpdateRequestDto("Implement Login Feature",
                "Develop and integrate the login feature for the application.",
                "HIGH", "NOT_STARTED", LocalDate.now(),
                3L, 2L, new HashSet<>());

        TaskDto expected = new TaskDto(4L, requestDto.name(),
                requestDto.description(), requestDto.priority(),
                requestDto.status(), requestDto.dueDate(),
                requestDto.projectId(), requestDto.userId(),
                new HashSet<>());

        String requestJson = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(put("/api/tasks/4")
                .content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        TaskDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                TaskDto.class);

        assertNotNull(actual);
        assertEquals(expected.id(), actual.id());
        assertEquals(expected.name(), actual.name());
        assertEquals(expected.description(), actual.description());
        assertEquals(expected.priority(), actual.priority());
        assertEquals(expected.status(), actual.status());
        assertEquals(expected.dueDate(), actual.dueDate());
        assertEquals(expected.projectId(), actual.projectId());
        assertEquals(expected.userId(), actual.userId());
    }

    @Test
    @DisplayName(value = "Delete Task - Success (No Content Returned)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteTaskById_ReturnNoContent() throws Exception {
        MvcResult result = mockMvc.perform(delete("/api/tasks/5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()).andReturn();

        int actual = result.getResponse().getStatus();

        assertEquals(204, actual);
    }

}
