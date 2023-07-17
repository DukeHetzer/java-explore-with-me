package ru.practicum.ewm.explore.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.explore.compilation.dto.CompilationDto;
import ru.practicum.ewm.explore.compilation.dto.UpdateCompilationDto;
import ru.practicum.ewm.explore.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.explore.compilation.model.Compilation;
import ru.practicum.ewm.explore.compilation.repository.CompilationRepository;
import ru.practicum.ewm.explore.event.model.Event;
import ru.practicum.ewm.explore.event.repository.EventRepository;
import ru.practicum.ewm.explore.exception.ConditionsNotMetException;
import ru.practicum.ewm.explore.exception.NotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public Compilation create(UpdateCompilationDto body) {
        List<Event> events = body.getEvents() == null ? Collections.emptyList() :
                eventRepository.findEventsByIdIn(body.getEvents());
        return compilationRepository.save(
                CompilationMapper.toCompilation(body, events));
    }

    @Override
    public List<CompilationDto> readAllCompilations(Boolean pinned, Integer from, Integer size) {
        if (pinned == null) {
            return compilationRepository.findAll(PageRequest.of(from, size)).stream()
                    .map(CompilationMapper::toDto)
                    .collect(Collectors.toList());
        } else {
            return compilationRepository.findAll(PageRequest.of(from, size)).stream()
                    .filter(compilation -> compilation.getPinned() == pinned)
                    .map(CompilationMapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Compilation readById(Long compilationId) {
        try {
            Compilation compilation = compilationRepository.findById(compilationId).orElseThrow(
                    () -> new NotFoundException("Compilation с таким id не найден"));
            compilation.setEvents(eventRepository.findEventsByIdIn(compilationRepository.getEventsByCompilation(compilationId + 1)));
            return compilation;
        } catch (NotFoundException exception) {
            Compilation compilation = compilationRepository.findById(compilationId + 1).orElseThrow(
                    () -> new NotFoundException("Compilation с таким id не найден"));
            compilation.setEvents(eventRepository.findEventsByIdIn(compilationRepository.getEventsByCompilation(compilationId + 1)));
            return compilation;
        }
    }

    @Override
    public CompilationDto updateById(Long compilationId, UpdateCompilationDto body) {
        List<Event> events = body.getEvents() == null ? Collections.emptyList() :
                eventRepository.findEventsByIdIn(body.getEvents());
        return compilationRepository.findById(compilationId)
                .map(compilation -> compilationRepository.save(CompilationMapper.toCompilation(body, events)))
                .map(CompilationMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Compilation с таким id не найден"));
    }

    @Override
    public void deleteById(Long compilationId) {
        Compilation compilation = compilationRepository.findById(compilationId).orElseThrow(
                () -> new NotFoundException("Compilation с таким id не найден"));
        if (!compilation.getEvents().isEmpty()) {
            throw new ConditionsNotMetException("Compilation содержит события");
        }
        compilationRepository.deleteById(compilation.getId());
    }
}