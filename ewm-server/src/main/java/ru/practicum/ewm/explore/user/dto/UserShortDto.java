package ru.practicum.ewm.explore.user.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class UserShortDto {
    private Long id;
    private String name;
}