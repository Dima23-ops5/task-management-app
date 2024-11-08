package com.example.task_management_app.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.task_management_app.dto.label.LabelCreateRequestDto;
import com.example.task_management_app.dto.label.LabelDto;
import com.example.task_management_app.dto.label.LabelUpdateRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LabelControllerTest {
    @Autowired
    protected static MockMvc mockMvc;
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
                    new ClassPathResource("/database/label/add-three-labels.sql"));
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
                    new ClassPathResource("/database/label/remove-all-labels.sql"));
        }
    }

    @AfterEach
    void deleteLabelFour(@Autowired DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("/database/label/remove-created-label.sql"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName(value = "Create a New Label - Success")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void createLabel() throws Exception {
        LabelCreateRequestDto requestDto = new LabelCreateRequestDto("IN_PROGRESS", "BLUE");

        LabelDto expected = new LabelDto(4L, requestDto.name(), requestDto.color());

        String requestJson = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/api/labels")
                .content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        LabelDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                LabelDto.class);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName(value = "Retrieve All Labels - Expected Three Labels")
    @WithMockUser(username = "user", roles = {"USER"})
    public void getAllLabels_ReturnThreeLabels_Ok() throws Exception {
        LabelDto label1 = new LabelDto(1L, "HIGH", "RED");
        LabelDto label2 = new LabelDto(2L, "MEDIUM", "YELLOW");
        LabelDto label3 = new LabelDto(3L, "LIGHT", "GREEN");

        List<LabelDto> expected = List.of(label1, label2, label3);

        MvcResult result = mockMvc.perform(get("/api/labels")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<LabelDto> actual = Arrays.stream(objectMapper.readValue(
                result.getResponse().getContentAsString(), LabelDto[].class))
                .sorted(Comparator.comparingLong(LabelDto::id))
                .toList();

        assertNotNull(actual);
        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
        assertEquals(expected.get(2), actual.get(2));
    }

    @Test
    @DisplayName(value = "Update Label - Success")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "/database/label/add-label.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateLabelById() throws Exception {
        LabelUpdateRequestDto requestDto = new LabelUpdateRequestDto(
                "Completed", "Green");

        LabelDto expected = new LabelDto(4L, requestDto.name(), requestDto.color());

        String requestJson = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(put("/api/labels/4").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        LabelDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                LabelDto.class);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName(value = "Delete Label - Success (No Content Returned)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteLabelById_ReturnNoContent() throws Exception {
        MvcResult result = mockMvc.perform(delete("/api/labels/4")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        int actual = result.getResponse().getStatus();
        assertEquals(204, actual);
    }
}
