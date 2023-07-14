package ru.practicum.ewm.stats.service.mapper;

import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.service.model.EndpointHit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EndpointHitMapper {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static EndpointHit map(EndpointHitDto dto) {
        Date date;
        try {
            date = formatter.parse(dto.getTimestamp());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return EndpointHit.builder()
                .app(dto.getApp())
                .ip(dto.getUri())
                .uri(dto.getUri())
                .timestamp(date)
                .build();
    }
}