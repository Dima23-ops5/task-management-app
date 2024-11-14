package com.example.task_management_app.service.internal;

import com.example.task_management_app.dto.comment.CommentCreateRequestDto;
import com.example.task_management_app.dto.comment.CommentDto;
import com.example.task_management_app.model.User;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    CommentDto addNewComment(CommentCreateRequestDto requestDto, User user);

    Page<CommentDto> findAllComments(@Positive Long taskId, Pageable pageable);
}
