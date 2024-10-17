package com.example.task_management_app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET is_deleted = TRUE WHERE id = ?")
@SQLRestriction(value = "is_deleted = FALSE")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String userName;
    @Column(nullable = false, unique = true)
    private String password;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @OneToMany(mappedBy = "assignee")
    private Set<Task> tasks = new HashSet<>();
    @OneToMany(mappedBy = "user")
    private Set<Comment> comments = new HashSet<>();
    @Column(nullable = false)
    private Boolean isDeleted = false;
}
