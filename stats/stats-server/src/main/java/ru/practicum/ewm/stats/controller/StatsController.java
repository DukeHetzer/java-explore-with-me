package ru.practicum.ewm.stats.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.EndpointStatsDto;
import ru.practicum.ewm.stats.model.EndpointHit;
import ru.practicum.ewm.stats.service.StatsServiceImpl;

import javax.validation.constraints.NotNull;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class StatsController {
    private final StatsServiceImpl service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/hit")
    public EndpointHit addHit(@RequestBody EndpointHitDto endpointHitDto) {
        return service.addHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<EndpointStatsDto> getStats(@RequestParam @NotNull String start,
                                           @RequestParam @NotNull String end,
                                           @RequestParam(defaultValue = "") List<String> uris,
                                           @RequestParam(defaultValue = "false") Boolean unique) {
        return service.getStats(start, end, uris, unique);
    }
}