package ru.practicum.ewm.explore.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.explore.enumerated.ParticipationStatus;
import ru.practicum.ewm.explore.enumerated.StateAction;
import ru.practicum.ewm.explore.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.explore.event.model.Event;
import ru.practicum.ewm.explore.event.repository.EventRepository;
import ru.practicum.ewm.explore.event.service.EventService;
import ru.practicum.ewm.explore.exception.ConflictRequestException;
import ru.practicum.ewm.explore.exception.NotFoundException;
import ru.practicum.ewm.explore.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.explore.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.explore.request.mapper.RequestMapper;
import ru.practicum.ewm.explore.request.model.ParticipationRequest;
import ru.practicum.ewm.explore.request.repository.RequestRepository;
import ru.practicum.ewm.explore.user.model.User;
import ru.practicum.ewm.explore.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.explore.enumerated.ParticipationStatus.CONFIRMED;
import static ru.practicum.ewm.explore.enumerated.ParticipationStatus.REJECTED;


@RequiredArgsConstructor
@Service
public class RequestServiceImpl implements RequestService {
    private final EventService eventService;
    private final UserService userService;
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getUserEventRequests(Long userId, Long eventId) {
        userService.getUserById(userId);
        eventService.findEventById(eventId);
        return requestRepository.findAllByEventId(eventId).stream()
                .map(RequestMapper::requestToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        userService.getUserById(userId);
        return requestRepository.findAllByRequester_Id(userId).stream()
                .map(RequestMapper::requestToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto addParticipationRequest(Long userId, Long eventId) {
        User user = userService.getUserById(userId);
        Event event = eventService.findEventById(eventId);
        if (!event.getState().equals(StateAction.PUBLISHED))
            throw new ConflictRequestException("Participation in unpublished events is denied");
        if (event.getInitiator().getId().equals(userId))
            throw new ConflictRequestException(
                    String.format("User id=%s is owner of event id=%s. Participation in your own events is denied",
                            userId, eventId));
        if (requestRepository.findAllByRequester_IdAndEvent_Id(userId, eventId).stream().findFirst().isPresent())
            throw new ConflictRequestException(
                    String.format("Request from user id=%s for event id=%s already exist.", userId, eventId));
        if (event.getParticipantLimit() != 0 &&
                requestRepository.countConfirmedRequests(eventId, ParticipationStatus.CONFIRMED)
                        >= event.getParticipantLimit())
            throw new ConflictRequestException("The limit on the number of participants has been exceeded");
        ParticipationRequest request = RequestMapper.getNewRequest(user, event);
        if (event.getParticipantLimit() == 0)
            request.setStatus(CONFIRMED);
        if (!event.getRequestModeration()) {
            request.setStatus(CONFIRMED);
        }
        return RequestMapper.requestToDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto cancelUserRequest(Long userId, Long requestId) {
        userService.getUserById(userId);
        ParticipationRequest request = getRequest(requestId);
        request.setStatus(ParticipationStatus.CANCELED);
        requestRepository.deleteById(requestId);
        return RequestMapper.requestToDto(request);
    }

    @Override
    public EventRequestStatusUpdateResult updateRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest eventDto) {
        userService.getUserById(userId);
        Event event = eventService.findEventById(eventId);
        List<ParticipationRequestDto> confirmedList = new ArrayList<>();
        List<ParticipationRequestDto> rejectedList = new ArrayList<>();
        EventRequestStatusUpdateResult updateResult = EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmedList)
                .rejectedRequests(rejectedList)
                .build();
        for (Long requestId : eventDto.getRequestIds()) {
            ParticipationRequest request = getRequest(requestId);
            if (request.getStatus().equals(CONFIRMED) && eventDto.getStatus().equals(REJECTED))
                throw new ConflictRequestException("Request is already accepted");
            request.setStatus(eventDto.getStatus());
            if (eventDto.getStatus() == CONFIRMED) {
                if (event.getConfirmedRequests() >= event.getParticipantLimit())
                    throw new ConflictRequestException("Requests limit");
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                eventRepository.save(event);
                List<ParticipationRequestDto> requestDtos = updateResult.getConfirmedRequests();
                requestDtos.add(RequestMapper.requestToDto(request));
                updateResult.setConfirmedRequests(requestDtos);
            } else if (eventDto.getStatus() == REJECTED) {
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                eventRepository.save(event);
                List<ParticipationRequestDto> requestDtos = updateResult.getRejectedRequests();
                requestDtos.add(RequestMapper.requestToDto(request));
                updateResult.setRejectedRequests(requestDtos);
            }
        }
        return updateResult;
    }

    private ParticipationRequest getRequest(Long reqId) {
        return requestRepository.findById(reqId).orElseThrow(() -> new NotFoundException(
                String.format("Request with id=%s was not found", reqId)));
    }
}