package ru.practicum.ewm.stats.mapper;

import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.model.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EndpointHitMapper {
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static EndpointHit toHit(EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                .id(endpointHitDto.getId())
                .uri(endpointHitDto.getUri())
                .app(endpointHitDto.getApp())
                .ip(endpointHitDto.getIp())
                .timestamp(LocalDateTime.parse(endpointHitDto.getTimestamp(), TIME_FORMATTER))
                .build();
    }
}