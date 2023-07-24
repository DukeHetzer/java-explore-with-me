package ru.practicum.ewm.explore.category.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class CategoryDto {
    private Long id;
    @NotBlank
    @Size(max = 50)
    private String name;
}