package com.example.task_management_app.repository;

import com.example.task_management_app.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @EntityGraph(attributePaths = {
            "user.role",
            "task.assignee.role",
            "task.project",
            "task.labels"
    })
    Page<Comment> findAllByTaskId(@Param("taskId") Long taskId, Pageable pageable);
}
