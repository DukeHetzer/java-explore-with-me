package ru.practicum.ewm.explore.category.service;

import ru.practicum.ewm.explore.category.dto.CategoryDto;
import ru.practicum.ewm.explore.category.dto.NewCategoryDto;
import ru.practicum.ewm.explore.category.model.Category;

import java.util.List;

public interface CategoryService {
    CategoryDto create(NewCategoryDto body);

    List<Category> readAllCategories(Integer from, Integer size);

    Category readById(Long catId);

    CategoryDto update(Long categoryId, CategoryDto body);

    void deleteById(Long catId);
}