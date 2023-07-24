package ru.practicum.ewm.explore.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.ewm.explore.event.model.Location;

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
    @Positive
    private Long id;
    @NotNull
    private Long category;
    @NotNull
    private Location location;
    @NotNull
    @Size(min = 20, max = 2000)
    private String annotation;
    @NotNull
    @Size(min = 20, max = 7000)
    private String description;
    @NotNull
    @Size(min = 3, max = 120)
    private String title;
    @Future
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Boolean paid;
    @PositiveOrZero
    private Integer participantLimit;
    private Boolean requestModeration;
}