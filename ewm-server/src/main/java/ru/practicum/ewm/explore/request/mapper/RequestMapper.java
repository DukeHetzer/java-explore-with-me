package ru.practicum.ewm.explore.request.mapper;

import ru.practicum.ewm.explore.enumerated.RequestStatus;
import ru.practicum.ewm.explore.event.model.Event;
import ru.practicum.ewm.explore.request.dto.RequestDto;
import ru.practicum.ewm.explore.request.model.Request;
import ru.practicum.ewm.explore.user.model.User;

import java.time.LocalDateTime;

public class RequestMapper {
    public static RequestDto toDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .created(request.getCreated())
                .build();
    }

    public static Request toRequest(User user, Event event) {
        return Request.builder()
                .requester(user)
                .event(event)
                .status(RequestStatus.PENDING)
                .created(LocalDateTime.now())
                .build();
    }
}