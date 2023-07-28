package ru.practicum.ewm.explore.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.explore.enumerated.RequestStatus;
import ru.practicum.ewm.explore.event.model.Event;
import ru.practicum.ewm.explore.request.dto.RequestDto;
import ru.practicum.ewm.explore.request.model.Request;
import ru.practicum.ewm.explore.user.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class RequestMapper {
    public static RequestDto toDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .created(request.getCreated())
                .status(request.getStatus())
                .build();
    }

    public static Request toRequest(User user, Event event) {
        return Request.builder()
                .event(event)
                .requester(user)
                .created(LocalDateTime.now())
                .status(RequestStatus.PENDING)
                .build();
    }
}