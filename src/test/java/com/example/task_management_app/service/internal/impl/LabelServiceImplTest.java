package com.example.task_management_app.service.internal.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.task_management_app.dto.label.LabelCreateRequestDto;
import com.example.task_management_app.dto.label.LabelDto;
import com.example.task_management_app.dto.label.LabelUpdateRequestDto;
import com.example.task_management_app.mapper.LabelMapper;
import com.example.task_management_app.model.Label;
import com.example.task_management_app.repository.LabelRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LabelServiceImplTest {
    @Mock
    private LabelRepository labelRepository;
    @Mock
    private LabelMapper labelMapper;
    @InjectMocks
    private LabelServiceImpl labelService;

    @Test
    @DisplayName(value = "Should save and return correct label")
    public void createLabel_Ok() {
        Label label = new Label();

        LabelCreateRequestDto requestDto = new LabelCreateRequestDto(label.getName(),
                label.getColor());

        LabelDto expected = labelToLabelDto(label);

        when(labelMapper.toEntity(requestDto)).thenReturn(label);
        when(labelRepository.save(label)).thenReturn(label);
        when(labelMapper.toDto(label)).thenReturn(expected);

        LabelDto actual = labelService.createLabel(requestDto);

        assertNotNull(actual);
        assertEquals(expected, actual);

        verify(labelMapper).toEntity(requestDto);
        verify(labelRepository).save(label);
        verify(labelMapper).toDto(label);
    }

    @Test
    @DisplayName(value = "Should find and return three labels")
    public void findAllLabels_ReturnThree_Ok() {
        Label label2 = new Label();
        label2.setId(2L);
        label2.setName("Urgent");
        label2.setColor("Red");

        Label label3 = new Label();
        label3.setId(3L);
        label3.setName("In Progress");
        label3.setColor("Blue");

        Label label1 = createTestLabel();

        LabelDto labelDto1 = labelToLabelDto(label1);
        LabelDto labelDto2 = labelToLabelDto(label2);
        LabelDto labelDto3 = labelToLabelDto(label3);

        List<Label> labels = List.of(label1, label2, label3);
        when(labelRepository.findAll()).thenReturn(labels);
        when(labelMapper.toDto(label1)).thenReturn(labelDto1);
        when(labelMapper.toDto(label2)).thenReturn(labelDto2);
        when(labelMapper.toDto(label3)).thenReturn(labelDto3);

        List<LabelDto> expected = List.of(labelDto1, labelDto2, labelDto3);
        List<LabelDto> actual = labelService.findAllLabels();

        assertNotNull(actual);
        assertEquals(3, actual.size());
        assertEquals(expected, actual);

        verify(labelMapper).toDto(label1);
        verify(labelMapper).toDto(label2);
        verify(labelMapper).toDto(label3);
    }

    @Test
    @DisplayName(value = "Should update and return updated correct label")
    public void updateLabel_Ok() {
        Label label = createTestLabel();

        Label afterUpdating = new Label();
        afterUpdating.setId(label.getId());
        afterUpdating.setName("Urgent");
        afterUpdating.setColor("Blue");

        LabelDto expected = labelToLabelDto(label);

        LabelUpdateRequestDto requestDto = new LabelUpdateRequestDto(
                afterUpdating.getName(), afterUpdating.getColor());
        when(labelRepository.findById(label.getId())).thenReturn(Optional.of(label));
        when(labelMapper.updateLabel(requestDto, label)).thenReturn(afterUpdating);
        when(labelRepository.save(afterUpdating)).thenReturn(afterUpdating);
        when(labelMapper.toDto(afterUpdating)).thenReturn(expected);

        LabelDto actual = labelService.updateLabelById(label.getId(), requestDto);

        assertNotNull(actual);
        assertEquals(expected, actual);

        verify(labelRepository).findById(label.getId());
        verify(labelMapper).updateLabel(requestDto, label);
        verify(labelMapper).toDto(afterUpdating);
    }

    private Label createTestLabel() {
        Label label = new Label();
        label.setId(1L);
        label.setName("Completed");
        label.setColor("Green");
        return label;
    }

    private LabelDto labelToLabelDto(Label label) {
        LabelDto labelDto = new LabelDto(
                label.getId(),
                label.getName(),
                label.getColor()
        );
        return labelDto;
    }
}
