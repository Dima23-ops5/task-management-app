package com.example.task_management_app.repository;

import com.example.task_management_app.model.Task;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId ")
    List<Task> findAllByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT t FROM Task t WHERE t.dueDate = :tomorrow")
    List<Task> findAllByDueDate(@Param("tomorrow") LocalDate tomorrow);
}
