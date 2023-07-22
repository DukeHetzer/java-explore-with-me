package ru.practicum.ewm.explore.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.explore.client.StatClient;
import ru.practicum.ewm.explore.enumerated.EventSort;
import ru.practicum.ewm.explore.event.dto.EventShortDto;
import ru.practicum.ewm.explore.event.model.Event;
import ru.practicum.ewm.explore.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/events")
public class PublicEventController {
    private final EventService eventService;
    private final StatClient statClient;

    @GetMapping
    public List<EventShortDto> readAllEvents(@RequestParam(defaultValue = "") String text,
                                             @RequestParam(required = false) List<Integer> categories,
                                             @RequestParam(required = false) Boolean paid,
                                             @RequestParam(required = false)
                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                             @RequestParam(required = false)
                                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                             @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                             @RequestParam(defaultValue = "EVENT_DATE") EventSort sort,
                                             @RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(defaultValue = "10") Integer size,
                                             HttpServletRequest request) {
        statClient.addStatHit(request);

        return eventService.readAllEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, from, size, sort);
    }

    @GetMapping("/{id}")
    public Event readEventById(@PathVariable Long id, HttpServletRequest request) {
        statClient.addStatHit(request);

        return eventService.readEvent(id);
    }
}