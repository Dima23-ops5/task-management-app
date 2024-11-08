package com.example.task_management_app.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.task_management_app.dto.comment.CommentCreateRequestDto;
import com.example.task_management_app.dto.comment.CommentDto;
import com.example.task_management_app.service.external.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentControllerTest {
    @Autowired
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private JavaMailSender mailSender;
    @MockBean
    private EmailService emailService;

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
                    new ClassPathResource(
                            "/database/comment/add-information-for-testing-controllers.sql"));
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
                    new ClassPathResource(
                            "/database/comment/remove-all-information-for-testing.sql"));
        }
    }

    @Test
    @DisplayName(value = "Create a New Comment - Success")
    @Sql(scripts = "/database/comment/remove-created-comment.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails("admin@example.com")
    public void createComment_Ok() throws Exception {
        CommentCreateRequestDto requestDto = new CommentCreateRequestDto(1L, "Test comment");
        CommentDto expected = new CommentDto(4L, 1L, 1L, requestDto.text(), LocalDateTime.now());

        String requestJson = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(post("/api/comments")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CommentDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                CommentDto.class);

        assertNotNull(actual);
        assertEquals(expected.id(), actual.id());
        assertEquals(expected.taskId(), actual.taskId());
        assertEquals(expected.userId(), actual.userId());
        assertEquals(expected.text(), actual.text());
    }

    @Test
    @DisplayName(value = "Retrieve All Comments Containing Task with ID = 1 - Expected Two Comment")
    @WithMockUser(username = "user", roles = {"USER"})
    public void getAllCommentsByTaskId_ReturnTwoComments_Ok() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        CommentDto comment1 = new CommentDto(1L, 1L,
                2L, "Added basic game mechanics, working on score calculation.",
                LocalDateTime.parse("2024-11-03 10:15:00", formatter));
        CommentDto comment2 = new CommentDto(2L, 1L, 1L,
                "Reviewed game logic, looks good. Consider adding a timer.",
                LocalDateTime.parse("2024-11-03 14:25:00", formatter));

        List<CommentDto> expected = List.of(comment1, comment2);

        MvcResult result = mockMvc.perform(get("/api/comments")
                .param("taskId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        List<CommentDto> actual = Arrays.stream(objectMapper.readValue(
                result.getResponse().getContentAsString(), CommentDto[].class))
                .sorted(Comparator.comparingLong(CommentDto::id))
                .toList();

        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
    }

}
