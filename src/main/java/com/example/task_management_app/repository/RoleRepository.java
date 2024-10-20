package com.example.task_management_app.repository;

import com.example.task_management_app.model.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleByRoleName(Role.RoleName name);
}
