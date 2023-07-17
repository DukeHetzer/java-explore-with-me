package ru.practicum.ewm.explore.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.explore.category.dto.CategoryDto;
import ru.practicum.ewm.explore.category.dto.NewCategoryDto;
import ru.practicum.ewm.explore.category.service.CategoryService;

import javax.validation.Valid;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    private final CategoryService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createAdminCategory(@RequestBody @Valid NewCategoryDto body) {
        return service.create(body);
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateAdminCategory(@PathVariable Long catId,
                                           @RequestBody @Valid CategoryDto body) {
        return service.update(catId, body);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Object> deleteAdminCategory(@PathVariable Long catId) {
        service.deleteById(catId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}