package ru.practicum.ewm.explore.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.explore.enumerated.StatusEvent;
import ru.practicum.ewm.explore.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.explore.event.model.Event;
import ru.practicum.ewm.explore.event.service.EventService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/events")
public class AdminEventController {
    private final EventService service;

    @GetMapping
    public List<Event> searchEvents(@RequestParam(required = false) List<Long> users,
                                    @RequestParam(required = false) List<StatusEvent> states,
                                    @RequestParam(required = false) List<Integer> categories,
                                    @RequestParam(required = false)
                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                    @RequestParam(required = false)
                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                    @RequestParam(defaultValue = "0") Integer from,
                                    @RequestParam(defaultValue = "10") Integer size) {
        return service.searchEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public Event updateEvent(@PathVariable Long eventId,
                             @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        return service.updateEvent(eventId, updateEventUserRequest);
    }
}