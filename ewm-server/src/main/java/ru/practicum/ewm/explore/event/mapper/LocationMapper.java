package ru.practicum.ewm.explore.event.mapper;

import ru.practicum.ewm.explore.event.dto.NewEventDto;
import ru.practicum.ewm.explore.event.model.Location;

public class LocationMapper {
    public static Location toLocation(NewEventDto eventDto) {
        return Location.builder()
                .lat(eventDto.getLocation().getLat())
                .lon(eventDto.getLocation().getLon())
                .build();
    }
}