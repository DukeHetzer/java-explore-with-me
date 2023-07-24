package ru.practicum.ewm.stats.service;

import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.EndpointStatsDto;
import ru.practicum.ewm.stats.model.EndpointHit;

import java.util.List;

public interface StatsService {
    EndpointHit addHit(EndpointHitDto endpointHitDto);

    List<EndpointStatsDto> getStats(String start, String end, List<String> uris, Boolean unique);
}