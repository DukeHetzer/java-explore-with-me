package ru.practicum.ewm.explore.user.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class UserShortDto {
    private Long id;
    private String name;
}