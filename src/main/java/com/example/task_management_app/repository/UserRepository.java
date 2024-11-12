package com.example.task_management_app.repository;

import com.example.task_management_app.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "role")
    Optional<UserDetails> findUserByEmail(String email);

    @EntityGraph(attributePaths = "role")
    Optional<User> findUserById(Long id);
}
