package com.example.task_management_app.repository;

import com.example.task_management_app.model.Task;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @EntityGraph(attributePaths = {"project", "assignee.role", "labels"})
    Page<Task> findAllByProjectId(@Param("projectId") Long projectId, Pageable pageable);

    @EntityGraph(attributePaths = {"project", "assignee.role", "labels"})
    Optional<Task> findById(Long id);

    @Query("SELECT t FROM Task t WHERE t.dueDate = :tomorrow")
    List<Task> findAllByDueDate(@Param("tomorrow") LocalDate tomorrow);
}
