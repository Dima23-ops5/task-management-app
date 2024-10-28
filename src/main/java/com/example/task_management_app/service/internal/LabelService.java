package com.example.task_management_app.service.internal;

import com.example.task_management_app.dto.label.LabelCreateRequestDto;
import com.example.task_management_app.dto.label.LabelDto;
import com.example.task_management_app.dto.label.LabelUpdateRequestDto;
import jakarta.validation.constraints.Positive;
import java.util.List;

public interface LabelService {
    LabelDto createLabel(LabelCreateRequestDto requestDto);

    List<LabelDto> findAllLabels();

    LabelDto updateLabelById(@Positive Long id, LabelUpdateRequestDto requestDto);

    void deleteLabelById(@Positive Long id);
}
