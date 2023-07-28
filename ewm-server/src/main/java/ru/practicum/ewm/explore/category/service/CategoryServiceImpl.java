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
import ru.practicum.ewm.explore.exception.ConflictException;
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
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        Category category = categoryRepository.save(toCategory(newCategoryDto));

        log.info(category + " создана");
        return toDto(category);
    }

    @Override
    public Category readCategory(Long catId) {
        return categoryRepository.findById(catId).orElseThrow(
                () -> new NotFoundException("Category с id=" + catId + " не найдена"));
    }

    @Override
    public List<Category> readAllCategories(Integer from, Integer size) {
        return categoryRepository.findAll(PageRequest.of(from, size, Sort.unsorted())).stream()
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        Category category = readCategory(catId);
        category.setName(categoryDto.getName());
        Category categoryUpdated = categoryRepository.save(category);

        log.info(categoryUpdated + " обновлена");
        return toDto(categoryUpdated);
    }

    @Override
    public void deleteCategory(Long catId) {
        if (categoryRepository.existsById(catId) &&
                eventRepository.findEventsByCategoryId(catId).stream().findAny().isEmpty()) {
            categoryRepository.deleteById(catId);

            log.info("Category с id={} удалена", catId);
        } else
            throw new ConflictException("Category с id=" + catId + " не существует");
    }
}