package com.example.task_management_app.mapper;

import com.example.task_management_app.config.MapperConfig;
import com.example.task_management_app.dto.comment.CommentCreateRequestDto;
import com.example.task_management_app.dto.comment.CommentDto;
import com.example.task_management_app.model.Comment;
import com.example.task_management_app.model.Task;
import com.example.task_management_app.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface CommentMapper {
    Comment toEntity(CommentCreateRequestDto requestDto);

    @Mapping(target = "taskId", source = "task", qualifiedByName = "getTaskId")
    @Mapping(target = "userId", source = "user", qualifiedByName = "getUserId")
    CommentDto toDto(Comment comment);

    @Named(value = "getTaskId")
    default Long getTaskId(Task task) {
        return task.getId();
    }

    @Named(value = "getUserId")
    default Long getUserId(User user) {
        return user.getId();
    }
}
