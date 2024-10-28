package com.example.task_management_app.mapper;

import com.example.task_management_app.config.MapperConfig;
import com.example.task_management_app.dto.label.LabelCreateRequestDto;
import com.example.task_management_app.dto.label.LabelDto;
import com.example.task_management_app.dto.label.LabelUpdateRequestDto;
import com.example.task_management_app.model.Label;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface LabelMapper {
    Label toEntity(LabelCreateRequestDto requestDto);

    LabelDto toDto(Label label);

    Label updateLabel(LabelUpdateRequestDto requestDto, @MappingTarget Label label);

    @Named(value = "labelsToDto")
    default Set<LabelDto> labelsToDto(Set<Label> labels) {
        return labels.stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }
}
