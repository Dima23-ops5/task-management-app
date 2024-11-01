package com.example.task_management_app.repository;

import com.example.task_management_app.model.Attachment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    @Query(value = "SELECT a FROM Attachment a WHERE a.task.id = :taskId ")
    List<Attachment> findAllByTaskId(@Param("taskId") Long taskId);
}
