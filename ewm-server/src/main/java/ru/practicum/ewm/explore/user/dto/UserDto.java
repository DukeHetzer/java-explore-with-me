package ru.practicum.ewm.explore.user.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class UserDto {
    private Long id;
    private String email;
    private String name;
}