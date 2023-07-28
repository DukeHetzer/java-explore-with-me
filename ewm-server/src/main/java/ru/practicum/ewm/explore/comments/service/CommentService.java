package ru.practicum.ewm.explore.comments.service;

import ru.practicum.ewm.explore.comments.dto.CommentDto;
import ru.practicum.ewm.explore.comments.dto.EventCommentDto;
import ru.practicum.ewm.explore.comments.dto.NewCommentDto;
import ru.practicum.ewm.explore.comments.model.Comment;
import ru.practicum.ewm.explore.event.model.Event;

import java.util.List;

public interface CommentService {
    Comment createComment(Long eventId, CommentDto commentDto, Event event);

    List<EventCommentDto> readComments(Long eventId);

    Comment updateComment(Long commId, Long userId, NewCommentDto newCommentDto);

    void deleteComment(Long commId, Long userId);
}