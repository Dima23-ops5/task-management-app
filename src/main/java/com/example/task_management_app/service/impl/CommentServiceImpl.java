package com.example.task_management_app.service.impl;

import com.example.task_management_app.dto.comment.CommentCreateRequestDto;
import com.example.task_management_app.dto.comment.CommentDto;
import com.example.task_management_app.mapper.CommentMapper;
import com.example.task_management_app.model.Comment;
import com.example.task_management_app.model.User;
import com.example.task_management_app.repository.CommentRepository;
import com.example.task_management_app.service.CommentService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentDto addNewComment(CommentCreateRequestDto requestDto, User user) {
        Comment comment = commentMapper.toEntity(requestDto);
        comment.setUser(user);
        comment.setTimestamp(LocalDateTime.now());
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentDto> findAllComments(Long taskId) {
        return commentRepository.findAllByTaskId(taskId);
    }
}
