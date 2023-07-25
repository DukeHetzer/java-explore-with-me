package ru.practicum.ewm.explore.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.explore.category.mapper.CategoryMapper;
import ru.practicum.ewm.explore.category.model.Category;
import ru.practicum.ewm.explore.enumerated.StatusEvent;
import ru.practicum.ewm.explore.event.dto.EventShortDto;
import ru.practicum.ewm.explore.event.dto.NewEventDto;
import ru.practicum.ewm.explore.event.model.Event;
import ru.practicum.ewm.explore.user.mapper.UserMapper;
import ru.practicum.ewm.explore.user.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class EventMapper {
    public static EventShortDto toDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .category(CategoryMapper.toDto(event.getCategory()))
                .initiator(UserMapper.toShortDto(event.getInitiator()))
                .annotation(event.getAnnotation())
                .confirmedRequests(event.getConfirmedRequests())
                .title(event.getTitle())
                .eventDate(event.getEventDate())
                .paid(event.getPaid())
                .views(event.getViews())
                .build();
    }

    public static Event toEvent(NewEventDto newEventDto, Category category, User user) {
        return Event.builder()
                .category(category)
                .initiator(user)
                .location(newEventDto.getLocation())
                .annotation(newEventDto.getAnnotation())
                .confirmedRequests(0L)
                .description(newEventDto.getDescription())
                .state(StatusEvent.PENDING)
                .title(newEventDto.getTitle())
                .createdOn(LocalDateTime.now())
                .eventDate(newEventDto.getEventDate())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .publishedOn(LocalDateTime.now())
                .requestModeration(newEventDto.getRequestModeration())
                .views(0L)
                .build();
    }
}