package com.example.task_management_app.service;

import com.example.task_management_app.dto.comment.CommentCreateRequestDto;
import com.example.task_management_app.dto.comment.CommentDto;
import com.example.task_management_app.model.User;
import jakarta.validation.constraints.Positive;
import java.util.List;

public interface CommentService {
    CommentDto addNewComment(CommentCreateRequestDto requestDto, User user);

    List<CommentDto> findAllComments(@Positive Long taskId);
}
