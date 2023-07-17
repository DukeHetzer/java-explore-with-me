package ru.practicum.ewm.explore.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.explore.enumerated.StateAction;
import ru.practicum.ewm.explore.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.explore.event.model.Event;
import ru.practicum.ewm.explore.event.service.EventService;
import ru.practicum.ewm.explore.util.OnCreate;
import ru.practicum.ewm.explore.util.OnUpdate;

import java.time.LocalDateTime;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/events")
public class AdminEventController {
    private final EventService service;

    @GetMapping
    public List<Event> searchAdminEvents(@RequestParam(name = "users", required = false) List<Long> users,
                                         @RequestParam(name = "states", required = false) List<StateAction> states,
                                         @RequestParam(name = "categories", required = false)
                                         List<Integer> categories,
                                         @RequestParam(name = "rangeStart", required = false)
                                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                         @RequestParam(name = "rangeEnd", required = false)
                                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                         @RequestParam(name = "from", defaultValue = "0") Integer from,
                                         @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return service.searchEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping(path = "/{eventId}")
    @Validated({OnCreate.class, OnUpdate.class})
    public Event updateAdminEvent(@PathVariable Long eventId,
                                  @RequestBody @Validated UpdateEventUserRequest body) {
        return service.updateEventByAdmin(eventId, body);
    }
}