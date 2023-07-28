package ru.practicum.ewm.explore.compilation.service;

import ru.practicum.ewm.explore.compilation.dto.CompilationDto;
import ru.practicum.ewm.explore.compilation.dto.UpdateCompilationDto;
import ru.practicum.ewm.explore.compilation.model.Compilation;

import java.util.List;

public interface CompilationService {
    Compilation createCompilation(UpdateCompilationDto updateCompilationDto);

    Compilation readCompilation(Long compId);

    List<CompilationDto> readAllCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto updateCompilation(Long compId, UpdateCompilationDto updateCompilationDto);

    void deleteCompilation(Long compId);
}