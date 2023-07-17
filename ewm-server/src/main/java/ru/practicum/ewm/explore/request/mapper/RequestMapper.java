package ru.practicum.ewm.explore.request.mapper;

import ru.practicum.ewm.explore.enumerated.ParticipationStatus;
import ru.practicum.ewm.explore.event.model.Event;
import ru.practicum.ewm.explore.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.explore.request.model.ParticipationRequest;
import ru.practicum.ewm.explore.user.model.User;

import java.time.LocalDateTime;

public class RequestMapper {
    public static ParticipationRequestDto requestToDto(ParticipationRequest request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .created(request.getCreated())
                .build();
    }

    public static ParticipationRequest getNewRequest(User user, Event event) {
        return ParticipationRequest.builder()
                .requester(user)
                .event(event)
                .status(ParticipationStatus.PENDING)
                .created(LocalDateTime.now())
                .build();
    }
}