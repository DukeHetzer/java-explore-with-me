package ru.practicum.ewm.explore.user.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class UserDto {
    private Long id;
    private String email;
    private String name;
}