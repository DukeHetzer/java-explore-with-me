package ru.practicum.ewm.explore.comments.dto;

import lombok.*;
import ru.practicum.ewm.explore.user.model.User;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class EventCommentDto {
    private Long id;
    private String description;
    private User user;
}