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
import ru.practicum.ewm.explore.exception.ConditionException;
import ru.practicum.ewm.explore.exception.NotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.explore.compilation.mapper.CompilationMapper.toCompilation;

@Slf4j
@RequiredArgsConstructor
@Service
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public Compilation createCompilation(UpdateCompilationDto body) {
        List<Event> events = body.getEvents() == null ? Collections.emptyList() :
                eventRepository.findEventsByIdIn(body.getEvents());
        Compilation compilation = compilationRepository.save(toCompilation(body, events));

        log.info(compilation + " создан");
        return compilation;
    }

    @Override
    public Compilation readCompilation(Long compId) {
        try {
            Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                    () -> new NotFoundException("Compilation с id=" + compId + " не найден"));
            compilation.setEvents(eventRepository.findEventsByIdIn(compilationRepository.getEventsByCompilation(compId + 1)));
            return compilation;
        } catch (NotFoundException exception) {
            Compilation compilation = compilationRepository.findById(compId + 1).orElseThrow(
                    () -> new NotFoundException("Compilation с id=" + compId + " не найден"));
            compilation.setEvents(eventRepository.findEventsByIdIn(compilationRepository.getEventsByCompilation(compId + 1)));

            return compilation;
        }
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
    public CompilationDto updateCompilation(Long compId, UpdateCompilationDto body) {
        List<Event> events = body.getEvents() == null ? Collections.emptyList() :
                eventRepository.findEventsByIdIn(body.getEvents());

        CompilationDto compilationDto = compilationRepository.findById(compId)
                .map(compilation -> compilationRepository.save(toCompilation(body, events)))
                .map(CompilationMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Compilation с id=" + compId + " не найден"));

        log.info(compilationDto + " обновлен");
        return compilationDto;
    }

    @Override
    public void deleteCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Compilation с id=" + compId + " не найден"));
        if (!compilation.getEvents().isEmpty()) {
            throw new ConditionException("Compilation с id=" + compId + " содержит события");
        }
        Long deletedCompilationId = compilation.getId();
        compilationRepository.deleteById(deletedCompilationId);
        log.info("Compilation с id={} удален", deletedCompilationId);
    }
}