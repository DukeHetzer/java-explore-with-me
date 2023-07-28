package ru.practicum.ewm.explore.event.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.explore.category.service.CategoryService;
import ru.practicum.ewm.explore.comments.dto.EventCommentDto;
import ru.practicum.ewm.explore.comments.service.CommentService;
import ru.practicum.ewm.explore.enumerated.EventSort;
import ru.practicum.ewm.explore.enumerated.RequestStatus;
import ru.practicum.ewm.explore.enumerated.StatusEvent;
import ru.practicum.ewm.explore.event.dto.NewEventDto;
import ru.practicum.ewm.explore.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.explore.event.model.Event;
import ru.practicum.ewm.explore.event.model.Location;
import ru.practicum.ewm.explore.event.repository.EventRepository;
import ru.practicum.ewm.explore.event.repository.LocationRepository;
import ru.practicum.ewm.explore.exception.BadRequestException;
import ru.practicum.ewm.explore.exception.ConflictException;
import ru.practicum.ewm.explore.exception.HasNoAccessException;
import ru.practicum.ewm.explore.exception.NotFoundException;
import ru.practicum.ewm.explore.request.repository.RequestRepository;
import ru.practicum.ewm.explore.user.service.UserService;

import javax.persistence.criteria.CriteriaBuilder;
import javax.xml.bind.ValidationException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.Specification.where;
import static ru.practicum.ewm.explore.event.mapper.EventMapper.toEvent;
import static ru.practicum.ewm.explore.event.mapper.LocationMapper.toLocation;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class EventServiceImpl implements EventService {
    private final UserService userService;
    private final CategoryService categoryService;
    private final CommentService commentService;
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;

    @Override
    @Transactional
    public Event createEvent(Long userId, NewEventDto newEventDto) {
        Location location = toLocation(newEventDto);

        if (newEventDto.getPaid() == null) {
            newEventDto.setPaid(false);
        }
        if (newEventDto.getParticipantLimit() == null) {
            newEventDto.setParticipantLimit(0);
        }
        if (newEventDto.getRequestModeration() == null) {
            newEventDto.setRequestModeration(true);
        }
        newEventDto.setLocation(locationRepository.save(location));

        Event event = eventRepository.save(toEvent(newEventDto,
                categoryService.readCategory(newEventDto.getCategory()),
                userService.readUser(userId)));

        log.info(event + " создано");
        return event;
    }

    @Override
    public Event readEvent(Long eventId) {
        Event event = findEventById(eventId);
        if (!event.getState().equals(StatusEvent.PUBLISHED)) {
            throw new NotFoundException("Event с id=" + eventId + " не найден");
        }
        Long requestCounts = requestRepository.countConfirmedRequests(eventId, RequestStatus.CONFIRMED);
        event.setConfirmedRequests(requestCounts);
        event.addView();

        eventRepository.save(event);
        return findEventById(eventId);
    }

    @SneakyThrows
    @Override
    public List<Event> readAllEvents(String text, List<Integer> categories, Boolean paid,
                                     LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                     Integer from, Integer size, EventSort sort) {
        if (rangeEnd != null && rangeStart != null && rangeEnd.isBefore(rangeStart)) {
            throw new ValidationException("Конец не может быть до начала");
        }
        switch (sort) {
            case VIEWS:
                return getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, from, size,
                        Comparator.comparing(Event::getViews));
            case EVENT_DATE:
                return getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, from, size,
                        Comparator.comparing(Event::getEventDate));
            default:
                return getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, from, size,
                        Comparator.comparing(Event::getId));
        }
    }

    @Override
    public Event readUserEventById(Long userId, Long eventId) {
        userService.readUser(userId);
        Event event = findEventById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new HasNoAccessException("User с id=" + userId + " не является основателем события с id=" + eventId);
        }
        return findEventById(eventId);
    }

    @Override
    public List<Event> readUserEvents(Long userId, Integer from, Integer size) {
        userService.readUser(userId);
        return eventRepository.findEventsByInitiatorId(userId, PageRequest.of(from, size, Sort.unsorted())).stream()
                .map(event -> toEventComments(event, commentService.readComments(event.getId())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Event updateEvent(Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        Event event = findEventById(eventId);

        if (updateEventUserRequest.getEventDate() != null && updateEventUserRequest.getEventDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Событие уже началось");
        }

        event.setCategory(Optional.ofNullable(updateEventUserRequest.getCategory()).map(categoryService::readCategory).orElse(event.getCategory()));
        event.setAnnotation(Optional.ofNullable(updateEventUserRequest.getAnnotation()).orElse(event.getAnnotation()));
        event.setDescription(Optional.ofNullable(updateEventUserRequest.getDescription()).orElse(event.getDescription()));
        event.setTitle(Optional.ofNullable(updateEventUserRequest.getTitle()).orElse(event.getTitle()));
        event.setEventDate(Optional.ofNullable(updateEventUserRequest.getEventDate()).orElse(event.getEventDate()));
        event.setPaid(Optional.ofNullable(updateEventUserRequest.getPaid()).orElse(event.getPaid()));
        event.setParticipantLimit(Optional.ofNullable(updateEventUserRequest.getParticipantLimit()).orElse(event.getParticipantLimit()));
        event.setRequestModeration(Optional.ofNullable(updateEventUserRequest.getRequestModeration()).orElse(event.getRequestModeration()));

        Optional.ofNullable(updateEventUserRequest.getLocation())
                .ifPresent(location -> {
                    Location eventLocation = event.getLocation();
                    eventLocation.setLat(location.getLat());
                    eventLocation.setLon(location.getLon());
                    event.setLocation(eventLocation);
                });

        Optional.ofNullable(updateEventUserRequest.getStateAction())
                .ifPresent(stateAction -> {
                    if (event.getState().equals(StatusEvent.PUBLISHED) && stateAction.equals(StatusEvent.REJECT_EVENT)) {
                        throw new ConflictException("Событие уже опубликовано");
                    }
                    if (event.getState().equals(StatusEvent.PUBLISHED) && stateAction.equals(StatusEvent.PUBLISH_EVENT)) {
                        throw new ConflictException("Событие уже опубликовано");
                    }
                    if (event.getState().equals(StatusEvent.REJECTED) && stateAction.equals(StatusEvent.PUBLISH_EVENT)) {
                        throw new ConflictException("Событие уже опубликовано");
                    }
                    if (event.getState().equals(StatusEvent.PUBLISH_EVENT) && stateAction.equals(StatusEvent.PUBLISHED)) {
                        throw new ConflictException("Событие уже опубликовано");
                    }
                    if (stateAction.equals(StatusEvent.SEND_TO_REVIEW)) {
                        event.setState(StatusEvent.PENDING);
                    }
                    if (stateAction.equals(StatusEvent.CANCEL_REVIEW)) {
                        event.setState(StatusEvent.CANCELED);
                    }
                    if (stateAction.equals(StatusEvent.REJECT_EVENT)) {
                        event.setState(StatusEvent.REJECTED);
                    }
                    if (stateAction.equals(StatusEvent.PUBLISH_EVENT)) {
                        event.setState(StatusEvent.PUBLISHED);
                    }
                });
        Event eventUpdated = eventRepository.save(event);
        List<EventCommentDto> comments = commentService.readComments(eventId);

        log.info(eventUpdated + " обновлено");
        return toEventComments(eventUpdated, comments);
    }

    @Override
    @Transactional
    public Event updateUserEvent(Long eventId, Long userId, UpdateEventUserRequest updateEventUserRequest) {
        Event event = findEventById(eventId);
        if (event.getState() != null && event.getState().equals(StatusEvent.PUBLISHED)) {
            throw new ConflictException("Событие уже опубликовано");
        }
        if (updateEventUserRequest.getStateAction() != null && updateEventUserRequest.getStateAction().equals(StatusEvent.PUBLISHED)) {
            throw new ConflictException("Событие уже опубликовано");
        }
        if (updateEventUserRequest.getStateAction() != null && updateEventUserRequest.getStateAction().equals(StatusEvent.CANCEL_REVIEW)) {
            event.setState(StatusEvent.CANCELED);
        }
        userService.readUser(userId);
        if (updateEventUserRequest.getEventDate() != null && updateEventUserRequest.getEventDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Событие уже началось");
        }
        Event eventUpdated = updateEventData(event, updateEventUserRequest);
        List<EventCommentDto> comments = commentService.readComments(eventId);

        log.info(eventUpdated + " обновлено");
        return toEventComments(eventUpdated, comments);
    }

    @Override
    public List<Event> searchEvents(List<Long> users, List<StatusEvent> states, List<Integer> categories,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                    Integer from, Integer size) {
        if (users != null || states != null || categories != null || rangeStart != null || rangeEnd != null) {
            return eventRepository.findAll(where(usersPredicate(users))
                                    .and(statesPredicate(states))
                                    .and(categoryPredicate(categories))
                                    .and((event, cq, cb) -> cb.greaterThan(event.get("eventDate"), rangeStart))
                                    .and((event, cq, cb) -> cb.lessThan(event.get("eventDate"), rangeEnd)),
                            PageRequest.of(from, size, Sort.unsorted()))
                    .stream()
                    .map(event -> toEventComments(event, commentService.readComments(event.getId())))
                    .collect(Collectors.toList());
        } else {
            return eventRepository.findAll(PageRequest.of(from, size, Sort.unsorted())).stream()
                    .map(event -> toEventComments(event, commentService.readComments(event.getId())))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Event findEventById(Long eventId) {
        List<EventCommentDto> comments = commentService.readComments(eventId);
        return toEventComments(eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event с id=" + eventId + " не найден")), comments);
    }

    private Specification<Event> textPredicate(String text) {
        return (event, cq, cb) -> {
            if (text == null) {
                return cb.isTrue(cb.literal(true));
            } else {
                return cb.or(cb.like(cb.lower(event.get("annotation")), "%" + text.toLowerCase() + "%"),
                        cb.like(cb.lower(event.get("description")), "%" + text.toLowerCase() + "%"));
            }
        };
    }

    private Specification<Event> availablePredicate(Boolean onlyAvailable) {
        return (event, cq, cb) -> {
            if (onlyAvailable != null && onlyAvailable) {
                return cb.or(cb.le(event.get("confirmedRequests"), event.get("participantLimit")),
                        cb.le(event.get("participantLimit"), 0));
            } else {
                return cb.isTrue(cb.literal(true));
            }
        };
    }

    private Specification<Event> categoryPredicate(List<Integer> categories) {
        return (event, cq, cb) -> {
            if (categories == null || categories.size() == 0) {
                return cb.isTrue(cb.literal(true));
            } else {
                CriteriaBuilder.In<Long> categoriesIds = cb.in(event.get("category"));
                for (long catId : categories) {
                    categoriesIds.value(catId);
                }
                return categoriesIds;
            }
        };
    }

    private Specification<Event> rangeStartPredicate(LocalDateTime rangeStart) {
        return (event, cq, cb) -> {
            if (rangeStart == null) {
                return cb.isTrue(cb.literal(true));
            } else {
                return cb.greaterThan(event.get("eventDate"), rangeStart);
            }
        };
    }

    private Specification<Event> paidPredicate(Boolean paid) {
        return (event, cq, cb) -> {
            if (paid == null) {
                return cb.isTrue(cb.literal(true));
            } else {
                return cb.equal(event.get("paid"), paid);
            }
        };
    }

    private Specification<Event> rangeEndPredicate(LocalDateTime rangeEnd) {
        return (event, cq, cb) -> {
            if (rangeEnd == null) {
                return cb.isTrue(cb.literal(true));
            } else {
                return cb.lessThan(event.get("eventDate"), rangeEnd);
            }
        };
    }

    private Specification<Event> statesPredicate(List<StatusEvent> states) {
        return (event, cq, cb) -> {
            if (states == null) {
                return cb.isTrue(cb.literal(true));
            } else {
                CriteriaBuilder.In<StatusEvent> cbStates = cb.in(event.get("state"));
                for (StatusEvent state : states) {
                    cbStates.value(state);
                }
                return cbStates;
            }
        };
    }

    private Specification<Event> usersPredicate(List<Long> users) {
        return (event, cq, cb) -> {
            if (users == null) {
                return cb.isTrue(cb.literal(true));
            } else {
                CriteriaBuilder.In<Long> usersIds = cb.in(event.get("initiator"));
                for (long userId : users) {
                    usersIds.value(userId);
                }
                return usersIds;
            }
        };
    }

    private List<Event> getEvents(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                  LocalDateTime rangeEnd, Boolean onlyAvailable, Integer from, Integer size,
                                  Comparator<Event> comparing) {
        return eventRepository.findAll(
                        where(textPredicate(text))
                                .and(categoryPredicate(categories))
                                .and(paidPredicate(paid))
                                .and(rangeEndPredicate(rangeEnd))
                                .and(rangeStartPredicate(rangeStart))
                                .and(availablePredicate(onlyAvailable)),
                        PageRequest.of(from, size, Sort.unsorted())).stream()
                .sorted(comparing)
                .map(event -> toEventComments(event, commentService.readComments(event.getId())))
                .collect(Collectors.toList());
    }

    private Event updateEventData(Event event, UpdateEventUserRequest dto) {
        return Event.builder()
                .category(dto.getCategory() != null ? categoryService.readCategory(dto.getCategory()) : event.getCategory())
                .initiator(event.getInitiator())
                .location(dto.getLocation() != null ? dto.getLocation() : event.getLocation())
                .annotation(dto.getAnnotation() != null ? dto.getAnnotation() : event.getAnnotation())
                .confirmedRequests(event.getConfirmedRequests())
                .description(dto.getDescription() != null ? dto.getDescription() : event.getDescription())
                .state(dto.getStateAction() == StatusEvent.SEND_TO_REVIEW ? StatusEvent.PENDING : event.getState())
                .title(dto.getTitle() != null ? dto.getTitle() : event.getTitle())
                .createdOn(event.getCreatedOn())
                .eventDate(dto.getEventDate() != null ? dto.getEventDate() : event.getEventDate())
                .paid(dto.getPaid() != null ? dto.getPaid() : event.getPaid())
                .participantLimit(dto.getParticipantLimit() != null ? dto.getParticipantLimit() : event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(dto.getRequestModeration() != null ? dto.getRequestModeration() : event.getRequestModeration())
                .views(event.getViews())
                .build();
    }

    public static Event toEventComments(Event event, List<EventCommentDto> comments) {
        event.setComments(comments);
        return event;
    }
}