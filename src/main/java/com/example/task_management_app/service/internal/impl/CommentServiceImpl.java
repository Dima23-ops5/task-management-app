package com.example.task_management_app.service.internal.impl;

import com.example.task_management_app.dto.comment.CommentCreateRequestDto;
import com.example.task_management_app.dto.comment.CommentDto;
import com.example.task_management_app.exception.EmailSendingException;
import com.example.task_management_app.exception.EntityNotFoundException;
import com.example.task_management_app.mapper.CommentMapper;
import com.example.task_management_app.model.Comment;
import com.example.task_management_app.model.Task;
import com.example.task_management_app.model.User;
import com.example.task_management_app.repository.CommentRepository;
import com.example.task_management_app.repository.TaskRepository;
import com.example.task_management_app.service.external.EmailService;
import com.example.task_management_app.service.internal.CommentService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final TaskRepository taskRepository;
    private final EmailService emailService;

    @Override
    @Transactional
    public CommentDto addNewComment(CommentCreateRequestDto requestDto, User user) {
        Comment comment = commentMapper.toEntity(requestDto);
        Task task = taskRepository.findById(requestDto.taskId()).orElseThrow(
                () -> new EntityNotFoundException("Cannot find task with id: "
                        + requestDto.taskId()));
        comment.setUser(user);
        comment.setTimestamp(LocalDateTime.now());
        comment.setTask(task);

        sendNotificationEmailAsync(task, comment, user);

        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public Page<CommentDto> findAllComments(Long taskId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findAllByTaskId(taskId, pageable);
        return comments.map(commentMapper::toDto);
    }

    @Async
    public void sendNotificationEmailAsync(Task task, Comment comment, User user) {
        String email = task.getAssignee().getEmail();
        String subject = "New comment for the task: " + task.getName();
        String text = "Hello, " + task.getAssignee().getUsername() + "! \n\n"
                + "For your task:" + task.getName() + " was added new comment. \n"
                + "Author's comment: " + user.getUsername() + "\n"
                + "Date and time: " + comment.getTimestamp() + "\n"
                + "Comment: " + comment.getText();
        try {
            emailService.sendEmail(email, subject, text);
        } catch (MessagingException e) {
            throw new EmailSendingException("Cannot send message to email: " + email, e);
        }
    }
}
