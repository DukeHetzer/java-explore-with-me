package ru.practicum.ewm.explore.event.service;

import ru.practicum.ewm.explore.enumerated.EventSort;
import ru.practicum.ewm.explore.enumerated.StateAction;
import ru.practicum.ewm.explore.event.dto.EventShortDto;
import ru.practicum.ewm.explore.event.dto.NewEventDto;
import ru.practicum.ewm.explore.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.explore.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    Event createUserEvent(Long userId, NewEventDto eventDto);

    Event readEventById(Long id);

    List<EventShortDto> readAllEvents(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                      LocalDateTime rangeEnd, Boolean onlyAvailable, Integer from, Integer size,
                                      EventSort sort);

    Event readUserEventById(Long userId, Long eventId);

    List<EventShortDto> readUserEvents(Long userId, Integer from, Integer size);

    Event updateEventByAdmin(Long eventId, UpdateEventUserRequest body);

    Event updateUserEvent(Long eventId, Long userId, UpdateEventUserRequest eventShortDto);

    List<Event> searchEvents(List<Long> users, List<StateAction> states, List<Integer> categories,
                             LocalDateTime rangeStart, LocalDateTime rangeEnd,
                             Integer from, Integer size);

    Event findEventById(Long eventId);
}