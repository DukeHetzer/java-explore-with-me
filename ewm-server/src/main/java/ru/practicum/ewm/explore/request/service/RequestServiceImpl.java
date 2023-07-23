package ru.practicum.ewm.explore.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.explore.enumerated.RequestStatus;
import ru.practicum.ewm.explore.enumerated.StatusEvent;
import ru.practicum.ewm.explore.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.explore.event.model.Event;
import ru.practicum.ewm.explore.event.repository.EventRepository;
import ru.practicum.ewm.explore.event.service.EventService;
import ru.practicum.ewm.explore.exception.ConflictException;
import ru.practicum.ewm.explore.exception.NotFoundException;
import ru.practicum.ewm.explore.request.dto.RequestDto;
import ru.practicum.ewm.explore.request.dto.RequestStatusUpdate;
import ru.practicum.ewm.explore.request.mapper.RequestMapper;
import ru.practicum.ewm.explore.request.model.Request;
import ru.practicum.ewm.explore.request.repository.RequestRepository;
import ru.practicum.ewm.explore.user.model.User;
import ru.practicum.ewm.explore.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.explore.enumerated.RequestStatus.CONFIRMED;
import static ru.practicum.ewm.explore.enumerated.RequestStatus.REJECTED;

@Slf4j
@RequiredArgsConstructor
@Service
public class RequestServiceImpl implements RequestService {
    private final EventService eventService;
    private final UserService userService;
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;

    @Override
    public RequestDto createRequest(Long userId, Long eventId) {
        User user = userService.readUser(userId);
        Event event = eventService.findEventById(eventId);
        if (!event.getState().equals(StatusEvent.PUBLISHED)) {
            throw new ConflictException("Участвовать в неопубликованных мероприятиях нельзя");
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Быть участником собственных мероприятий нельзя");
        }
        if (requestRepository.findAllByRequesterIdAndEventId(userId, eventId).stream().findFirst().isPresent()) {
            throw new ConflictException("Запрос уже был отправлен");
        }
        if (event.getParticipantLimit() != 0 &&
                requestRepository.countConfirmedRequests(eventId, RequestStatus.CONFIRMED)
                        >= event.getParticipantLimit()) {
            throw new ConflictException("Превышен лимит на количество участников");
        }
        Request request = RequestMapper.toRequest(user, event);
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            request.setStatus(CONFIRMED);
        }
        Request requestCreated = requestRepository.save(request);

        log.info(requestCreated + " создан");
        return RequestMapper.toDto(requestCreated);
    }

    @Override
    public List<RequestDto> readUserRequests(Long userId, Long eventId) {
        userService.readUser(userId);
        eventService.findEventById(eventId);
        return requestRepository.findAllByEventId(eventId).stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RequestDto> readAllUserRequests(Long userId) {
        userService.readUser(userId);
        return requestRepository.findAllByRequesterId(userId).stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestStatusUpdate updateRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest eventDto) {
        userService.readUser(userId);
        Event event = eventService.findEventById(eventId);

        RequestStatusUpdate requestUpdated = RequestStatusUpdate.builder()
                .confirmedRequests(new ArrayList<>())
                .rejectedRequests(new ArrayList<>())
                .build();

        for (Long requestId : eventDto.getRequestIds()) {
            Request request = pickRequest(requestId);
            if (request.getStatus().equals(CONFIRMED) && eventDto.getStatus().equals(REJECTED)) {
                throw new ConflictException("Запрос уже одобрен");
            }
            request.setStatus(eventDto.getStatus());
            if (eventDto.getStatus() == CONFIRMED) {
                if (event.getConfirmedRequests() >= event.getParticipantLimit())
                    throw new ConflictException("Превышен лимит на количество участников");
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                eventRepository.save(event);
                List<RequestDto> requestDtoList = requestUpdated.getConfirmedRequests();
                requestDtoList.add(RequestMapper.toDto(request));
                requestUpdated.setConfirmedRequests(requestDtoList);
            } else if (eventDto.getStatus() == REJECTED) {
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                eventRepository.save(event);
                List<RequestDto> requestDtoList = requestUpdated.getRejectedRequests();
                requestDtoList.add(RequestMapper.toDto(request));
                requestUpdated.setRejectedRequests(requestDtoList);
            }
        }
        log.info(requestUpdated + " обновлен");
        return requestUpdated;
    }

    @Override
    public RequestDto deleteUserRequest(Long userId, Long requestId) {
        userService.readUser(userId);
        Request request = pickRequest(requestId);
        request.setStatus(RequestStatus.CANCELED);
        requestRepository.deleteById(requestId);

        log.info("Request с id={} удален", requestId);
        return RequestMapper.toDto(request);
    }

    private Request pickRequest(Long reqId) {
        return requestRepository.findById(reqId).orElseThrow(
                () -> new NotFoundException("Request с id=" + reqId + " не найден"));
    }
}