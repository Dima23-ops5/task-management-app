package com.example.task_management_app.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.task_management_app.model.Comment;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
@Sql(scripts = "classpath:/database/comment/add-three-comments.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:/database/comment/remove-all-comments.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
@Sql(scripts = "classpath:/database/task/remove-all-tasks.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
@Sql(scripts = "classpath:/database/project/remove-all-projects.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class CommentRepositoryTest {
    private static final int FIRST_COMMENT_INDEX = 0;
    private static final int SECOND_COMMENT_INDEX = 1;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    @DisplayName(value = "Should return two comments for task with id = 1")
    public void findAllCommentsByTaskId_Ok() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Comment comment1 = new Comment();
        comment1.setId(1L);
        comment1.setText("Added basic game mechanics, working on score calculation.");
        comment1.setTimestamp(LocalDateTime.parse("2024-11-03 10:15:00", formatter));

        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setText("Reviewed game logic, looks good. Consider adding a timer.");
        comment2.setTimestamp(LocalDateTime.parse("2024-11-03 14:25:00", formatter));

        List<Comment> expected = List.of(comment1, comment2);
        List<Comment> actual = commentRepository.findAllByTaskId(1L);

        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertEquals(expected.get(FIRST_COMMENT_INDEX).getId(),
                actual.get(FIRST_COMMENT_INDEX).getId());
        assertEquals(expected.get(FIRST_COMMENT_INDEX).getText(),
                actual.get(FIRST_COMMENT_INDEX).getText());
        assertEquals(expected.get(SECOND_COMMENT_INDEX).getTimestamp(),
                actual.get(SECOND_COMMENT_INDEX).getTimestamp());
        assertEquals(expected.get(SECOND_COMMENT_INDEX).getId(),
                actual.get(SECOND_COMMENT_INDEX).getId());
        assertEquals(expected.get(SECOND_COMMENT_INDEX).getText(),
                actual.get(SECOND_COMMENT_INDEX).getText());
        assertEquals(expected.get(SECOND_COMMENT_INDEX).getTimestamp(),
                actual.get(SECOND_COMMENT_INDEX).getTimestamp());
    }

    @Test
    @DisplayName("Should return empty list for task whithout comments")
    public void findAllCommentsByTaskId_EmptyList() {
        List<Comment> actual = commentRepository.findAllByTaskId(2L);

        assertEquals(0, actual.size());
    }
}
