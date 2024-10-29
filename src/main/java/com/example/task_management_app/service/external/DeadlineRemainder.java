package com.example.task_management_app.service.external;

import com.example.task_management_app.exception.EmailSendingException;
import com.example.task_management_app.model.Project;
import com.example.task_management_app.model.Task;
import com.example.task_management_app.model.User;
import com.example.task_management_app.repository.ProjectRepository;
import com.example.task_management_app.repository.TaskRepository;
import jakarta.mail.MessagingException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeadlineRemainder {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 0 * * *")
    public void projectDeadLineRemainder() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Project> projects = projectRepository.findAllByEndDate(tomorrow);
        for (Project project : projects) {
            Set<User> users = project.getUsers();
            for (User user : users) {
                String email = user.getEmail();
                String subject = "Reminder: the project with name:" + project.getName()
                        + " deadline is tomorrow";
                String text = "Hello, " + user.getUsername() + "\n"
                        + "just a reminder that tomorrow is the deadline on the project: "
                        + project.getName() + ".\n"
                        + "Please be sure to complete it on time.";
                try {
                    emailService.sendEmail(email, subject, text);
                } catch (MessagingException e) {
                    throw new EmailSendingException("Cannot send email to: " + email, e);
                }
            }
        }
    }

    @Scheduled(cron = "0 10 0 * * *")
    public void taskDeadLineRemainder() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Task> tasks = taskRepository.findAllByDueDate(tomorrow);
        for (Task task : tasks) {
            String email = task.getAssignee().getEmail();
            String subject = "Reminder: the task with name:" + task.getName()
                    + " deadline is tomorrow";
            String text = "Hello, " + task.getAssignee().getUsername() + ",\n"
                    + "just a reminder that tomorrow is the deadline on the project: "
                    + task.getName() + ".\n"
                    + "Please be sure to complete it on time.";
            try {
                emailService.sendEmail(email, subject, text);
            } catch (MessagingException e) {
                throw new EmailSendingException("Cannot send email to: " + email, e);
            }
        }
    }
}
