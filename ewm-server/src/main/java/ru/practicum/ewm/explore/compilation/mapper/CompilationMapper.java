package ru.practicum.ewm.explore.compilation.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.explore.compilation.dto.CompilationDto;
import ru.practicum.ewm.explore.compilation.dto.UpdateCompilationDto;
import ru.practicum.ewm.explore.compilation.model.Compilation;
import ru.practicum.ewm.explore.event.mapper.EventMapper;
import ru.practicum.ewm.explore.event.model.Event;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
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

    public static Compilation toCompilation(UpdateCompilationDto dto, List<Event> events) {
        return Compilation.builder()
                .pinned(dto.getPinned())
                .title(dto.getTitle())
                .events(events)
                .build();
    }
}