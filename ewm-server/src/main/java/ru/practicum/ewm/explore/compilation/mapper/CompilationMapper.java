package ru.practicum.ewm.explore.compilation.mapper;

import ru.practicum.ewm.explore.compilation.dto.CompilationDto;
import ru.practicum.ewm.explore.compilation.dto.UpdateCompilationDto;
import ru.practicum.ewm.explore.compilation.model.Compilation;
import ru.practicum.ewm.explore.event.mapper.EventMapper;
import ru.practicum.ewm.explore.event.model.Event;

import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static CompilationDto toDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .events(compilation.getEvents().stream()
                        .map(EventMapper::toDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public static Compilation toCompilation(UpdateCompilationDto body, List<Event> events) {
        return Compilation.builder()
                .pinned(body.getPinned())
                .title(body.getTitle())
                .events(events)
                .build();
    }
}