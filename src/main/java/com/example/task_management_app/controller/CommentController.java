package com.example.task_management_app.controller;

import com.example.task_management_app.dto.comment.CommentCreateRequestDto;
import com.example.task_management_app.dto.comment.CommentDto;
import com.example.task_management_app.model.User;
import com.example.task_management_app.service.internal.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Comments management", description = "Endpoints for managing comments")
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add new comment to a task",
            description = "Adding and return new comment for a task")
    @PreAuthorize(value = "hasAuthority('USER')")
    public CommentDto addComment(@RequestBody @Valid CommentCreateRequestDto requestDto,
                                 Authentication authentication) {
        User user = (User)authentication.getPrincipal();
        return commentService.addNewComment(requestDto, user);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all task's comments",
            description = "Retrieve and return all task's comments")
    @PreAuthorize(value = "hasAuthority('USER')")
    public List<CommentDto> getAllComments(@RequestParam @Positive Long taskId,
                                           @ParameterObject @PageableDefault Pageable pageable) {
        return commentService.findAllComments(taskId, pageable);
    }
}
