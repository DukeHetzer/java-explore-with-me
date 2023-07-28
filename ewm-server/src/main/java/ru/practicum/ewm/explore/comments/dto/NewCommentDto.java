package ru.practicum.ewm.explore.comments.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class NewCommentDto {
    @NotBlank
    @Size(min = 3, max = 7000)
    private String description;
}