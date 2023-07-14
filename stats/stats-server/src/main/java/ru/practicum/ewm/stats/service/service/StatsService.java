package ru.practicum.ewm.stats.service.service;

import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.EndpointStatsDto;
import ru.practicum.ewm.stats.service.model.EndpointHit;

import java.util.List;

public interface StatsService {
    EndpointHit addHit(EndpointHitDto endpointHitDto);

    List<EndpointStatsDto> getStats(String start, String end, List<String> uris, Boolean unique);
}