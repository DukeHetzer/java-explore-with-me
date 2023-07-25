package ru.practicum.ewm.explore.comments.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.explore.comments.dto.CommentDto;
import ru.practicum.ewm.explore.comments.dto.EventCommentDto;
import ru.practicum.ewm.explore.comments.dto.NewCommentDto;
import ru.practicum.ewm.explore.comments.model.Comment;
import ru.practicum.ewm.explore.comments.repository.CommentRepository;
import ru.practicum.ewm.explore.event.model.Event;
import ru.practicum.ewm.explore.exception.BadRequestException;
import ru.practicum.ewm.explore.exception.NotFoundException;
import ru.practicum.ewm.explore.user.model.User;
import ru.practicum.ewm.explore.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.practicum.ewm.explore.comments.mapper.CommentMapper.commentToEventDto;
import static ru.practicum.ewm.explore.comments.mapper.CommentMapper.dtoToComment;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final UserService userService;
    private final CommentRepository commentRepository;

    @Override
    public Comment createComment(Long eventId, CommentDto commentDto, Event event) {
        Comment comment = dtoToComment(commentDto, userService.readUser(commentDto.getUser()), event);
        commentRepository.save(comment);

        log.info(comment + " создан");
        return comment;
    }

    @Override
    public List<EventCommentDto> readComments(Long eventId) {
        List<EventCommentDto> list = new ArrayList<>();
        for (Comment comment : commentRepository.findAllByEventId(eventId)) {
            EventCommentDto eventCommentDto = commentToEventDto(comment, comment.getUser());
            list.add(eventCommentDto);
        }
        return list;
    }

    @Override
    public Comment updateComment(Long commId, Long userId, NewCommentDto newCommentDto) {
        Comment comment = commentRepository.findById(commId).orElseThrow(
                () -> new NotFoundException("Comment с id=" + commId + " не найден"));
        User user = userService.readUser(userId);
        if (!Objects.equals(comment.getUser().getId(), user.getId())) {
            throw new BadRequestException("Пользователь не может изменять комментарий, так как не является его автором");
        }
        comment.setDescription(newCommentDto.getDescription());
        commentRepository.save(comment);

        log.info(comment + " обновлен");
        return comment;
    }

    @Override
    public void deleteComment(Long commId, Long userId) {
        Comment comment = commentRepository.findById(commId).orElseThrow(
                () -> new NotFoundException("Comment с id=" + commId + " не найден"));
        User user = userService.readUser(userId);
        if (!Objects.equals(comment.getUser().getId(), user.getId())) {
            throw new BadRequestException("Пользователь не может удалять комментарий, так как не является его автором");
        }
        commentRepository.delete(comment);

        log.info(comment + " удален");
    }
}