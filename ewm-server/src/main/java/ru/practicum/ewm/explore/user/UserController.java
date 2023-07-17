package ru.practicum.ewm.explore.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.explore.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.explore.event.dto.EventShortDto;
import ru.practicum.ewm.explore.event.dto.NewEventDto;
import ru.practicum.ewm.explore.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.explore.event.model.Event;
import ru.practicum.ewm.explore.event.service.EventService;
import ru.practicum.ewm.explore.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.explore.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.explore.request.service.RequestService;
import ru.practicum.ewm.explore.util.OnCreate;

import javax.validation.Valid;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/users/{userId}")
public class UserController {
    private final EventService eventService;
    private final RequestService requestService;

    @GetMapping("/events")
    public List<EventShortDto> getUserEvents(@PathVariable Long userId,
                                             @RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(defaultValue = "10") Integer size) {
        return eventService.readUserEvents(userId, from, size);
    }

    @PostMapping("/events")
    @Validated({OnCreate.class})
    @ResponseStatus(HttpStatus.CREATED)
    public Event addUserEvent(@PathVariable Long userId,
                              @RequestBody @Valid NewEventDto event) {
        return eventService.createUserEvent(userId, event);
    }

    @GetMapping("/events/{eventId}")
    public Event getUserEventById(@PathVariable Long userId,
                                  @PathVariable Long eventId) {
        return eventService.readUserEventById(userId, eventId);
    }

    @PatchMapping("/events/{eventId}")
    public Event updateUserEvent(@PathVariable Long eventId,
                                 @PathVariable Long userId,
                                 @RequestBody @Valid UpdateEventUserRequest event) {
        return eventService.updateUserEvent(eventId, userId, event);
    }

    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> getUserEventsRequests(@PathVariable Long userId,
                                                               @PathVariable Long eventId) {
        return requestService.getUserEventRequests(userId, eventId);
    }

    @PatchMapping("/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateUserEventById(@PathVariable Long userId,
                                                              @PathVariable Long eventId,
                                                              @RequestBody @Valid EventRequestStatusUpdateRequest eventBody) {
        return requestService.updateRequest(userId, eventId, eventBody);
    }

    @GetMapping("/requests")
    public List<ParticipationRequestDto> getUserEventsRequests(@PathVariable Long userId) {
        return requestService.getUserRequests(userId);
    }

    @PostMapping("/requests")
    @Validated({OnCreate.class})
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addUserEventRequest(@PathVariable Long userId,
                                                       @RequestParam Long eventId) {
        return requestService.addParticipationRequest(userId, eventId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    @Validated({OnCreate.class})
    public ParticipationRequestDto cancelUserEvent(@PathVariable Long userId,
                                                   @PathVariable Long requestId) {
        return requestService.cancelUserRequest(userId, requestId);
    }
}