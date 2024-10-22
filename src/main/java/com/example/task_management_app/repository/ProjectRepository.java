package com.example.task_management_app.repository;

import com.example.task_management_app.model.Project;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p FROM Project p JOIN p.users u WHERE u.id = :userId ")
    List<Project> findAllByUserId(@Param("userId")Long userId);

    Optional<Project> findProjectById(Long id);
}
