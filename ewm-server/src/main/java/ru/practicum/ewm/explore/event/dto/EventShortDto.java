package ru.practicum.ewm.explore.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.ewm.explore.category.dto.CategoryDto;
import ru.practicum.ewm.explore.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@Builder
public class EventShortDto {
    private Long id;
    private CategoryDto category;
    private UserShortDto initiator;
    private String annotation;
    private Long confirmedRequests;
    private String title;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Boolean paid;
    private Long views;
}