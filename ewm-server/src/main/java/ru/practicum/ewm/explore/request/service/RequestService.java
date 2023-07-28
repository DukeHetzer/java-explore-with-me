package ru.practicum.ewm.explore.request.service;

import ru.practicum.ewm.explore.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.explore.request.dto.RequestDto;
import ru.practicum.ewm.explore.request.dto.RequestStatusUpdate;

import java.util.List;

public interface RequestService {
    RequestDto createRequest(Long userId, Long eventId);

    List<RequestDto> readUserRequests(Long userId, Long eventId);

    List<RequestDto> readAllUserRequests(Long userId);

    RequestStatusUpdate updateRequest(Long userId, Long reqId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    RequestDto deleteUserRequest(Long userId, Long reqId);
}