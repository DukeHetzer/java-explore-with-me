package ru.practicum.ewm.explore.comments.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.explore.comments.dto.CommentDto;
import ru.practicum.ewm.explore.comments.dto.EventCommentDto;
import ru.practicum.ewm.explore.comments.model.Comment;
import ru.practicum.ewm.explore.event.model.Event;
import ru.practicum.ewm.explore.user.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {
    public static Comment dtoToComment(CommentDto commentDto, User user, Event event) {
        return Comment.builder()
                .user(user)
                .event(event)
                .description(commentDto.getDescription())
                .date(LocalDateTime.now())
                .build();
    }

    public static EventCommentDto commentToEventDto(Comment comment, User user) {
        return EventCommentDto.builder()
                .id(comment.getId())
                .user(user)
                .description(comment.getDescription())
                .build();
    }
}