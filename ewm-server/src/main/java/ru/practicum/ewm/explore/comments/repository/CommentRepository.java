package ru.practicum.ewm.explore.comments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.explore.comments.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByEventId(Long eventId);
}