package com.example.task_management_app.service.impl;

import com.example.task_management_app.dto.label.LabelCreateRequestDto;
import com.example.task_management_app.dto.label.LabelDto;
import com.example.task_management_app.dto.label.LabelUpdateRequestDto;
import com.example.task_management_app.mapper.LabelMapper;
import com.example.task_management_app.model.Label;
import com.example.task_management_app.repository.LabelRepository;
import com.example.task_management_app.service.LabelService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LabelServiceImpl implements LabelService {
    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;

    @Override
    public LabelDto createLabel(LabelCreateRequestDto requestDto) {
        Label label = labelMapper.toEntity(requestDto);
        return labelMapper.toDto(labelRepository.save(label));
    }

    @Override
    public List<LabelDto> findAllLabels() {
        return labelRepository.findAll().stream()
                .map(labelMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public LabelDto updateLabelById(Long id, LabelUpdateRequestDto requestDto) {
        Label label = labelRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Cannot find label with id: " + id)
        );
        Label labelAfterUpdating = labelMapper.updateLabel(requestDto, label);
        return labelMapper.toDto(labelRepository.save(labelAfterUpdating));
    }

    @Override
    public void deleteLabelById(Long id) {
        labelRepository.deleteById(id);
    }
}
