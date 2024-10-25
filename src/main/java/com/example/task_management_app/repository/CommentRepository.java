package com.example.task_management_app.repository;

import com.example.task_management_app.dto.comment.CommentDto;
import com.example.task_management_app.model.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.task.id = :taskId")
    List<CommentDto> findAllByTaskId(@Param("taskId") Long taskId);
}
