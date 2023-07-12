package ru.practicum.ewm.stats.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.EndpointStatsDto;
import ru.practicum.ewm.stats.service.model.EndpointHit;
import ru.practicum.ewm.stats.service.service.StatsServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsServiceImpl statsService;

    @PostMapping("/hit")
    public EndpointHit addHit(@RequestBody EndpointHitDto endpointHitDto) {
        return statsService.addHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<EndpointStatsDto> getStats(@RequestParam String start,
                                           @RequestParam String end,
                                           @RequestParam(required = false) List<String> uris,
                                           @RequestParam(defaultValue = "false") Boolean unique) {
        return statsService.getStats(start, end, uris, unique);
    }
}