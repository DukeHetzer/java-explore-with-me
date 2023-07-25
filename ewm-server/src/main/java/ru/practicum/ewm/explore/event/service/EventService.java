package ru.practicum.ewm.explore.event.service;

import ru.practicum.ewm.explore.enumerated.EventSort;
import ru.practicum.ewm.explore.enumerated.StatusEvent;
import ru.practicum.ewm.explore.event.dto.NewEventDto;
import ru.practicum.ewm.explore.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.explore.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    Event createEvent(Long userId, NewEventDto newEventDto);

    Event readEvent(Long eventId);

    List<Event> readAllEvents(String text,
                              List<Integer> categories,
                              Boolean paid,
                              LocalDateTime rangeStart,
                              LocalDateTime rangeEnd,
                              Boolean onlyAvailable,
                              Integer from,
                              Integer size,
                              EventSort sort);

    Event readUserEventById(Long userId, Long eventId);

    List<Event> readUserEvents(Long userId, Integer from, Integer size);

    Event updateEvent(Long eventId, UpdateEventUserRequest updateEventUserRequest);

    Event updateUserEvent(Long eventId, Long userId, UpdateEventUserRequest updateEventUserRequest);

    List<Event> searchEvents(List<Long> users,
                             List<StatusEvent> states,
                             List<Integer> categories,
                             LocalDateTime rangeStart,
                             LocalDateTime rangeEnd,
                             Integer from,
                             Integer size);

    Event findEventById(Long eventId);
}