package com.example.task_management_app.repository;

import com.example.task_management_app.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    @Sql(scripts = "classpath:/database/role/add-tesk-role.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:/database/role/remove-test-role.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findRoleByRoleName() {
        Role excepted = new Role();
        excepted.setId(1L);
        excepted.setRoleName(Role.RoleName.USER);

        Role actual = roleRepository.findRoleByRoleName(Role.RoleName.USER).get();
        assertNotNull(actual);
        assertEquals(excepted, actual);
    }
  
}