package ru.practicum.ewm.explore.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.explore.category.dto.CategoryDto;
import ru.practicum.ewm.explore.category.mapper.CategoryMapper;
import ru.practicum.ewm.explore.category.model.Category;
import ru.practicum.ewm.explore.category.service.CategoryService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class PublicCategoryController {
    private final CategoryService service;

    @GetMapping
    public List<Category> readAllCategories(@RequestParam(defaultValue = "0") Integer from,
                                            @RequestParam(defaultValue = "10") Integer size) {
        return service.readAllCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto readCategory(@PathVariable Long catId) {
        Category category = service.readCategory(catId);
        return CategoryMapper.toDto(category);
    }
}