package ru.practicum.ewm.explore.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.explore.category.dto.CategoryDto;
import ru.practicum.ewm.explore.category.dto.NewCategoryDto;
import ru.practicum.ewm.explore.category.service.CategoryService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    private final CategoryService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CategoryDto createCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        return service.createCategory(newCategoryDto);
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(@PathVariable Long catId,
                                      @RequestBody @Valid CategoryDto categoryDto) {
        return service.updateCategory(catId, categoryDto);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Object> deleteCategory(@PathVariable Long catId) {
        service.deleteCategory(catId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}