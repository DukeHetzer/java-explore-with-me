package ru.practicum.ewm.explore.comments.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.explore.comments.dto.CommentDto;
import ru.practicum.ewm.explore.comments.dto.EventCommentDto;
import ru.practicum.ewm.explore.comments.dto.NewCommentDto;
import ru.practicum.ewm.explore.comments.model.Comment;
import ru.practicum.ewm.explore.comments.service.CommentService;
import ru.practicum.ewm.explore.event.service.EventService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;
    private final EventService eventService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{commId}")
    public Comment createComment(@PathVariable Long commId,
                                 @RequestBody @Valid CommentDto commentDto) {
        return commentService.createComment(commId, commentDto, eventService.readEvent(commId));
    }

    @GetMapping("/{eventId}")
    public List<EventCommentDto> readComments(@PathVariable Long eventId) {
        return commentService.readComments(eventId);
    }

    @PatchMapping
    public Comment updateComment(@RequestBody @Valid NewCommentDto commentDto,
                                 @RequestParam Long comment,
                                 @RequestParam Long user) {
        return commentService.updateComment(comment, user, commentDto);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteComment(@RequestParam Long comment,
                                                @RequestParam Long user) {
        commentService.deleteComment(comment, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}