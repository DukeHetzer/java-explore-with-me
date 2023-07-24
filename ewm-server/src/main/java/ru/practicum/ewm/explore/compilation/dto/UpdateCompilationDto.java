package ru.practicum.ewm.explore.compilation.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class UpdateCompilationDto {
    @Builder.Default()
    private Boolean pinned = false;
    @NotBlank
    @NotNull
    @Size(min = 3, max = 50)
    private String title;
    private Set<Long> events;
}