package ru.practicum.ewm.explore.client;

import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@UtilityClass
public class HitMapper {
    public static Hit toHit(HttpServletRequest request) {
        return Hit.builder()
                .ip(request.getRemoteAddr())
                .app("ewm-main-service")
                .uri(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
    }
}