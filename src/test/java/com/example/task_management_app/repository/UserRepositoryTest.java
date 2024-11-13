package com.example.task_management_app.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.task_management_app.exception.EntityNotFoundException;
import com.example.task_management_app.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName(value = "Should find user by valid email")
    @Sql(scripts = "classpath:/database/user/add-test-user.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:/database/user/remove-test-user.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findUserByValidEmail_Ok() {
        User expected = createUserBob();

        User actual = (User)userRepository.findUserByEmail(expected.getEmail()).get();

        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
    }

    @Test
    @DisplayName("Should find user by valid ID")
    @Sql(scripts = "classpath:/database/user/add-test-user.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:/database/user/remove-test-user.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findUserById_Ok() {
        User expected = createUserBob();
        Long id = 1L;

        User actual = userRepository.findUserById(id).get();

        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when user not found by ID")
    public void findUserByNotExistedId_throwEntityNotFoundException() {
        Long id = 5L;
        String expectedMessage = "Cannot find user with id: " + id;

        EntityNotFoundException actualException = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> userRepository.findUserById(id).orElseThrow(() ->
                        new EntityNotFoundException(expectedMessage))
        );

        assertEquals(expectedMessage, actualException.getMessage());
    }

    private User createUserBob() {
        User bob = new User();
        bob.setId(1L);
        bob.setUserName("bobUser");
        bob.setPassword("bobPassword12345");
        bob.setEmail("bobemail@gmail.com");
        bob.setFirstName("Bob");
        bob.setLastName("Peterson");

        return bob;
    }
}
