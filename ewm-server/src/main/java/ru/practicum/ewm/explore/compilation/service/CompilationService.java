package ru.practicum.ewm.explore.compilation.service;

import ru.practicum.ewm.explore.compilation.dto.CompilationDto;
import ru.practicum.ewm.explore.compilation.dto.UpdateCompilationDto;
import ru.practicum.ewm.explore.compilation.model.Compilation;

import java.util.List;

public interface CompilationService {
    Compilation create(UpdateCompilationDto body);

    List<CompilationDto> readAllCompilations(Boolean pinned, Integer from, Integer size);

    Compilation readById(Long compilationId);

    CompilationDto updateById(Long compilationId, UpdateCompilationDto body);

    void deleteById(Long compilationId);
}