package ru.practicum.ewm.explore.event.mapper;

import ru.practicum.ewm.explore.category.mapper.CategoryMapper;
import ru.practicum.ewm.explore.category.model.Category;
import ru.practicum.ewm.explore.enumerated.StateAction;
import ru.practicum.ewm.explore.event.dto.EventShortDto;
import ru.practicum.ewm.explore.event.dto.NewEventDto;
import ru.practicum.ewm.explore.event.model.Event;
import ru.practicum.ewm.explore.user.mapper.UserMapper;
import ru.practicum.ewm.explore.user.model.User;

import java.time.LocalDateTime;

public class EventMapper {
    public static EventShortDto eventToShortDto(Event event) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.userToShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static Event newDtoToEvent(NewEventDto newEventDto, Category category, User user) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .confirmedRequests(0L)
                .createdOn(LocalDateTime.now())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .initiator(user)
                .location(newEventDto.getLocation())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .publishedOn(LocalDateTime.now())
                .requestModeration(newEventDto.getRequestModeration())
                .state(StateAction.PENDING)
                .title(newEventDto.getTitle())
                .views(0L)
                .build();
    }
}