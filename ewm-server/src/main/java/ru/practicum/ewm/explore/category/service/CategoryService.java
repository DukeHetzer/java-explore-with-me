package ru.practicum.ewm.explore.category.service;

import ru.practicum.ewm.explore.category.dto.CategoryDto;
import ru.practicum.ewm.explore.category.dto.NewCategoryDto;
import ru.practicum.ewm.explore.category.model.Category;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(NewCategoryDto body);

    Category readCategory(Long catId);

    List<Category> readAllCategories(Integer from, Integer size);

    CategoryDto updateCategory(Long categoryId, CategoryDto body);

    void deleteCategory(Long catId);
}