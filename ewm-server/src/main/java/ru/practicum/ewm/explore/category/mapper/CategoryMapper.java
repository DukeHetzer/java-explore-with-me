package ru.practicum.ewm.explore.category.mapper;

import ru.practicum.ewm.explore.category.dto.CategoryDto;
import ru.practicum.ewm.explore.category.dto.NewCategoryDto;
import ru.practicum.ewm.explore.category.model.Category;

public class CategoryMapper {
    public static CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Category toCategory(NewCategoryDto dto) {
        return Category.builder()
                .name(dto.getName())
                .build();
    }
}