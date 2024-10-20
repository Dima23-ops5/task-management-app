package com.example.task_management_app.repository;

import com.example.task_management_app.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<UserDetails> findUserByEmail(String email);
}
