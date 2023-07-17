package ru.practicum.ewm.explore.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.explore.category.dto.CategoryDto;
import ru.practicum.ewm.explore.category.dto.NewCategoryDto;
import ru.practicum.ewm.explore.category.model.Category;
import ru.practicum.ewm.explore.category.repository.CategoryRepository;
import ru.practicum.ewm.explore.event.repository.EventRepository;
import ru.practicum.ewm.explore.exception.ConflictRequestException;
import ru.practicum.ewm.explore.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.explore.category.mapper.CategoryMapper.toCategory;
import static ru.practicum.ewm.explore.category.mapper.CategoryMapper.toDto;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto create(NewCategoryDto body) {
        return toDto(categoryRepository.save(toCategory(body)));
    }

    @Override
    public Category readById(Long catId) {
        return categoryRepository.findById(catId).orElseThrow(
                () -> new NotFoundException("Category с таким id не найдена"));
    }

    @Override
    public List<Category> readAllCategories(Integer from, Integer size) {
        return categoryRepository.findAll(PageRequest.of(from, size, Sort.unsorted())).stream()
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto update(Long categoryId, CategoryDto body) {
        Category category = readById(categoryId);
        category.setName(body.getName());
        return toDto(categoryRepository.save(category));
    }

    @Override
    public void deleteById(Long catId) {
        if (categoryRepository.existsById(catId) && eventRepository.findEventsByCategoryId(catId).stream().findAny().isEmpty()) {
            categoryRepository.deleteById(catId);
        } else
            throw new ConflictRequestException("Category с таким id не существует");
    }
}