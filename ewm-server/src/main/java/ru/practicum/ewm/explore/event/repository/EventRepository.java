package ru.practicum.ewm.explore.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.ewm.explore.event.model.Event;

import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    List<Event> findEventsByCategoryId(Long catId);

    List<Event> findEventsByInitiatorId(Long userId, Pageable pageable);

    List<Event> findEventsByIdIn(Set<Long> events);
}