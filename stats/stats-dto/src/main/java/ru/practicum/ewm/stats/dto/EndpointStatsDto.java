package ru.practicum.ewm.stats.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EndpointStatsDto {
    private String app;
    private String uri;
    private Integer hits;
}