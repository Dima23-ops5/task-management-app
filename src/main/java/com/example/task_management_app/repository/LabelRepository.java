package com.example.task_management_app.repository;

import com.example.task_management_app.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabelRepository extends JpaRepository<Label, Long> {
}
