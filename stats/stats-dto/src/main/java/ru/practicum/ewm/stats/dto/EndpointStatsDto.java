package ru.practicum.ewm.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class EndpointStatsDto {
    private String app;
    private String uri;
    private Long hits;
}