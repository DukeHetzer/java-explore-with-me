package ru.practicum.ewm.stats.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EndpointHitDto {
    private Long id;
    @NotBlank
    @NotBlank
    private String app;
    private String uri;
    @NotBlank
    private String ip;
    private String timestamp;
    private Integer hits;
}