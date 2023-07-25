package ru.practicum.ewm.stats.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EndpointStatsDto {
    private String app;
    private String uri;
    private Integer hits;
}