package ru.practicum.ewm.explore.compilation.dto;

import lombok.*;
import ru.practicum.ewm.explore.event.dto.EventShortDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class CompilationDto {
    private Long id;
    private Boolean pinned;
    @Size(min = 3, max = 50)
    private String title;
    @NotEmpty
    private List<EventShortDto> events;
}