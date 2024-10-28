package com.example.task_management_app.controller;

import com.example.task_management_app.dto.label.LabelCreateRequestDto;
import com.example.task_management_app.dto.label.LabelDto;
import com.example.task_management_app.dto.label.LabelUpdateRequestDto;
import com.example.task_management_app.service.internal.LabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Labels management",
        description = "Endpoints for managing labels")
@RestController
@RequestMapping("/api/labels")
@RequiredArgsConstructor
public class LabelController {
    private final LabelService labelService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new label",
            description = "Creating a new label and saving it to database")
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    public LabelDto createLabel(@RequestBody @Valid LabelCreateRequestDto requestDto) {
        return labelService.createLabel(requestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all labels",
            description = "Retrieve and return all labels")
    @PreAuthorize(value = "hasAuthority('USER')")
    public List<LabelDto> getAllLabels() {
        return labelService.findAllLabels();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update label by id",
            description = "Updating and return updated label by it's id")
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    public LabelDto updateLabel(@PathVariable @Positive Long id,
                                @RequestBody @Valid LabelUpdateRequestDto requestDto) {
        return labelService.updateLabelById(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete label by id",
            description = "Delete label by it's id and return no content status")
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    public void deleteLabel(@PathVariable @Positive Long id) {
        labelService.deleteLabelById(id);
    }

}
