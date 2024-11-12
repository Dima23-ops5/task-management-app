package com.example.task_management_app.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.task_management_app.dto.user.UserDto;
import com.example.task_management_app.dto.user.UserUpdateRequestDto;
import com.example.task_management_app.dto.user.UserUpdateRoleDto;
import com.example.task_management_app.model.Role;
import com.example.task_management_app.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.sql.Connection;
import java.sql.SQLException;
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
class UserControllerTest {
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
                    new ClassPathResource("/database/user/add-three-users.sql"));
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
                    new ClassPathResource("/database/user/remove-all-users.sql"));
        }
    }

    @Test
    @DisplayName(value = "Update User's Role - Success")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateUserRoleByUserId_Ok() throws Exception {
        UserUpdateRoleDto userUpdateRoleDto = new UserUpdateRoleDto("ADMIN");

        UserDto expected = new UserDto(2L, "asmith", "Alice","Smith","asmith@example.com");

        String requestJson = objectMapper.writeValueAsString(userUpdateRoleDto);

        MvcResult result = mockMvc.perform(put("/users/2/role")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        UserDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                UserDto.class);

        assertNotNull(actual);
        assertEquals(expected.id(), actual.id());
        assertEquals(expected.userName(), actual.userName());
        assertEquals(expected.firstName(), actual.firstName());
        assertEquals(expected.lastName(), actual.lastName());
        assertEquals(expected.email(), actual.email());
    }

    @Test
    @DisplayName(value = "Retrieve current authenticated User by ID - Success")
    @WithUserDetails("bwilliams@example.com")
    public void getAllInformationAboutCurrentUser_Ok() throws Exception {
        User user = new User();
        user.setId(3L);
        user.setUserName("bwilliams");
        user.setFirstName("Bob");
        user.setLastName("Williams");
        user.setEmail("bwilliams@example.com");
        user.setPassword("myPass#2024");

        Role role = new Role();
        role.setId(1L);
        role.setRoleName(Role.RoleName.USER);
        user.setRole(Set.of(role));

        UserDto expected = new UserDto(user.getId(),
                user.getUserName(), user.getFirstName(),
                user.getLastName(), user.getEmail());

        MvcResult result = mockMvc.perform(get("/users/me")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        UserDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                UserDto.class);

        assertNotNull(actual);
        assertEquals(expected.id(), actual.id());
        assertEquals(expected.userName(), actual.userName());
        assertEquals(expected.firstName(), actual.firstName());
        assertEquals(expected.lastName(), actual.lastName());
        assertEquals(expected.email(), actual.email());
    }

    @Test
    @DisplayName(value = "Update User's information - Success")
    @WithUserDetails("user@example.com")
    @Sql(scripts = "/database/user/add-user-for-updating.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void updateUser_UpdatedCurrentAuthenticatedUser_Ok() throws Exception {
        UserUpdateRequestDto requestDto = new UserUpdateRequestDto("userUpdated",
                "userUpdated", "JustUserUpdated",
                "userUpdated@example.com");

        UserDto expected = new UserDto(4L, requestDto.userName(),
                 requestDto.firstName(), requestDto.lastName(),
                 requestDto.email());

        String requestJson = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(patch("/users/me")
                 .content(requestJson)
                 .contentType(MediaType.APPLICATION_JSON))
                 .andExpect(status().isOk())
                 .andReturn();

        UserDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                UserDto.class);

        assertNotNull(actual);
        assertEquals(expected.id(), actual.id());
        assertEquals(expected.userName(), actual.userName());
        assertEquals(expected.firstName(), actual.firstName());
        assertEquals(expected.lastName(), actual.lastName());
        assertEquals(expected.email(), actual.email());
    }

}
