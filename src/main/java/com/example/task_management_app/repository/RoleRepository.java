package com.example.task_management_app.repository;

import com.example.task_management_app.model.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("SELECT r FROM Role r WHERE r.roleName = :roleName")
    Optional<Role> findRoleByRoleName(@Param("roleName") Role.RoleName roleName);
}
