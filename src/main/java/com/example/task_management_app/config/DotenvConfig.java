package com.example.task_management_app.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotenvConfig {
    @Bean
    public Dotenv dotenv() {
        try {
            return Dotenv.load();
        } catch (Exception e) {
            System.out.println("Failed to load dotenv: " + e);
            return Dotenv.configure().ignoreIfMalformed().load();
        }
    }
}
