package com.example.task_management_app;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TaskManagementAppApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().load();
        System.setProperty("MYSQL_ROOT_USERNAME", dotenv.get("MYSQL_ROOT_USERNAME"));
        System.setProperty("MYSQL_ROOT_PASSWORD", dotenv.get("MYSQL_ROOT_PASSWORD"));
        System.setProperty("JWT_EXPIRATION", dotenv.get("JWT_EXPIRATION"));
        System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
        System.setProperty("MAIL_USERNAME", dotenv.get("MAIL_USERNAME"));
        System.setProperty("MAIL_PASSWORD", dotenv.get("MAIL_PASSWORD"));
        SpringApplication.run(TaskManagementAppApplication.class, args);
    }

}
