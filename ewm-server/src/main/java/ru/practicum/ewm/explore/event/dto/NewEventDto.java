package ru.practicum.ewm.explore.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.ewm.explore.event.model.Location;
import ru.practicum.ewm.explore.util.OnCreate;
import ru.practicum.ewm.explore.util.OnUpdate;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Jacksonized
public class NewEventDto {
    @Positive(groups = {OnUpdate.class})
    private Long id;
    @NotNull(groups = {OnUpdate.class, OnCreate.class})
    private Long category;
    @NotNull(groups = {OnCreate.class})
    private Location location;
    @NotNull(groups = {OnUpdate.class, OnCreate.class})
    @Size(min = 20, max = 2000)
    private String annotation;
    @NotNull(groups = {OnUpdate.class, OnCreate.class})
    @Size(min = 20, max = 7000)
    private String description;
    @NotNull(groups = {OnUpdate.class, OnCreate.class})
    @Size(min = 3, max = 120)
    private String title;
    @Future(groups = {OnUpdate.class, OnCreate.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Boolean paid;
    @PositiveOrZero(groups = {OnUpdate.class, OnCreate.class})
    private Integer participantLimit;
    private Boolean requestModeration;
}