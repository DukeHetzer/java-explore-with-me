package ru.practicum.ewm.explore.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.explore.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.explore.event.dto.NewEventDto;
import ru.practicum.ewm.explore.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.explore.event.model.Event;
import ru.practicum.ewm.explore.event.service.EventService;
import ru.practicum.ewm.explore.request.dto.RequestDto;
import ru.practicum.ewm.explore.request.dto.RequestStatusUpdate;
import ru.practicum.ewm.explore.request.service.RequestService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users/{userId}")
public class PublicUserController {
    private final EventService eventService;
    private final RequestService requestService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/events")
    public Event createUserEvent(@PathVariable Long userId,
                                 @RequestBody @Valid NewEventDto newEventDto) {
        return eventService.createEvent(userId, newEventDto);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/requests")
    public RequestDto createUserRequest(@PathVariable Long userId,
                                        @RequestParam Long eventId) {
        return requestService.createRequest(userId, eventId);
    }

    @GetMapping("/events/{eventId}")
    public Event readUserEventById(@PathVariable Long userId,
                                   @PathVariable Long eventId) {
        return eventService.readUserEventById(userId, eventId);
    }

    @GetMapping("/events")
    public List<Event> readUserEvents(@PathVariable Long userId,
                                      @RequestParam(defaultValue = "0") Integer from,
                                      @RequestParam(defaultValue = "10") Integer size) {
        return eventService.readUserEvents(userId, from, size);
    }

    @GetMapping("/events/{eventId}/requests")
    public List<RequestDto> readUserRequests(@PathVariable Long userId,
                                             @PathVariable Long eventId) {
        return requestService.readUserRequests(userId, eventId);
    }

    @GetMapping("/requests")
    public List<RequestDto> readAllUserRequests(@PathVariable Long userId) {
        return requestService.readAllUserRequests(userId);
    }

    @PatchMapping("/events/{eventId}")
    public Event updateUserEvent(@PathVariable Long eventId,
                                 @PathVariable Long userId,
                                 @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        return eventService.updateUserEvent(eventId, userId, updateEventUserRequest);
    }

    @PatchMapping("/events/{eventId}/requests")
    public RequestStatusUpdate updateUserRequest(@PathVariable Long userId,
                                                 @PathVariable Long eventId,
                                                 @RequestBody @Valid EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        return requestService.updateRequest(userId, eventId, eventRequestStatusUpdateRequest);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public RequestDto deleteUserEvent(@PathVariable Long userId,
                                      @PathVariable Long requestId) {
        return requestService.deleteUserRequest(userId, requestId);
    }
}