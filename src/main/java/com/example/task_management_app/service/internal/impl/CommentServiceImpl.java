package com.example.task_management_app.service.internal.impl;

import com.example.task_management_app.dto.comment.CommentCreateRequestDto;
import com.example.task_management_app.dto.comment.CommentDto;
import com.example.task_management_app.exception.EmailSendingException;
import com.example.task_management_app.mapper.CommentMapper;
import com.example.task_management_app.model.Comment;
import com.example.task_management_app.model.Task;
import com.example.task_management_app.model.User;
import com.example.task_management_app.repository.CommentRepository;
import com.example.task_management_app.repository.TaskRepository;
import com.example.task_management_app.service.external.EmailService;
import com.example.task_management_app.service.internal.CommentService;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
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
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentDto> findAllComments(Long taskId) {
        return commentRepository.findAllByTaskId(taskId).stream()
                .map(commentMapper::toDto)
                .toList();
    }
}
